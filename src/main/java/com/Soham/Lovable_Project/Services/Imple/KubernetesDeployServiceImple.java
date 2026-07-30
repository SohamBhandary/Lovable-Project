package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.Deploy.DeployResponse;
import com.Soham.Lovable_Project.Services.DeploymentService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesDeployServiceImple implements DeploymentService {

    private final KubernetesClient client;
    private final StringRedisTemplate redisTemplate;

    private static final String NAMESPACE = "projectlovable";
    private static final String POOL_LABEL = "status";
    private static final String PROJECT_LABEL = "project-id";

    private static final String IDLE = "idle";
    private static final String BUSY = "busy";

    private static final String RUNNER_CONTAINER = "runner";
    private static final String SYNCER_CONTAINER = "syncer";

    private static final String REVERSE_PROXY_PORT = "8090";

    @Override
    public DeployResponse deploy(Long projectId) {
        String domain = "project-" + projectId + ".app.domain.com";
        Pod existingPod = findActivePod(projectId);

        if (existingPod != null) {
            registerRoute(domain, existingPod);
            return new DeployResponse("http://" + domain + ":" + REVERSE_PROXY_PORT);
        }

        return claimAndStartNewPod(projectId, domain);
    }

    private DeployResponse claimAndStartNewPod(Long projectId, String domain) {
        Pod pod = client.pods()
                .inNamespace(NAMESPACE)
                .withLabel(POOL_LABEL, IDLE)
                .list()
                .getItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No idle runners available. Please scale up the runner-pool."));

        String podName = pod.getMetadata().getName();
        log.info("Claiming pod {} for project {}", podName, projectId);

        client.pods()
                .inNamespace(NAMESPACE)
                .withName(podName)
                .edit(p -> {
                    p.getMetadata().getLabels().put(POOL_LABEL, BUSY);
                    p.getMetadata().getLabels().put(PROJECT_LABEL, projectId.toString());
                    return p;
                });

        try {
            //-------------------------------
            // 1. Initial Sync
            //-------------------------------
            String initialSyncCmd = String.format("mc mirror --overwrite myminio/projects/%d/ /app/", projectId);
            log.info("Syncing project files inside container: {}", SYNCER_CONTAINER);
            execCommand(podName, SYNCER_CONTAINER, "sh", "-c", initialSyncCmd);

            //-------------------------------
            // 2. Watch for future changes
            //-------------------------------
            String watchCmd = String.format("nohup mc mirror --overwrite --watch myminio/projects/%d/ /app/ >/app/sync.log 2>&1 &", projectId);
            log.info("Starting background file watcher sync...");
            execCommand(podName, SYNCER_CONTAINER, "sh", "-c", watchCmd);

            //-------------------------------
            // 3. Wait until package.json is fully populated
            //-------------------------------
            String waitCmd = """
                    for i in $(seq 1 30); do
                      if [ -s /app/package.json ]; then
                        echo "package.json is ready and has content!"
                        exit 0
                      fi
                      echo "Waiting for project sync to completely finish ($i)..."
                      sleep 1
                    done
                    exit 1
                    """;
            log.info("Waiting for files to finish streaming into the runner space...");
            execCommand(podName, RUNNER_CONTAINER, "sh", "-c", waitCmd);

            //-------------------------------
            // 4. Clean & Install dependencies
            //-------------------------------
            log.info("Wiping stale contexts and installing node dependencies cleanly...");
            execCommand(podName, RUNNER_CONTAINER, "sh", "-c", "cd /app && rm -rf node_modules && npm install");

            //-------------------------------
            // 5. Start Vite directly with broad binding
            //-------------------------------
            log.info("Starting Vite server natively in background...");
            execCommand(podName, RUNNER_CONTAINER, "sh", "-c", "cd /app && nohup ./node_modules/.bin/vite --host 0.0.0.0 --port 5173 >/app/dev.log 2>&1 &");

            //-------------------------------
            // 6. Give Vite time to boot
            //-------------------------------
            Thread.sleep(3000);

            //-------------------------------
            // 7. Refresh pod info
            //-------------------------------
            pod = client.pods()
                    .inNamespace(NAMESPACE)
                    .withName(podName)
                    .get();

            registerRoute(domain, pod);
            log.info("Deployment completely successful!");

            return new DeployResponse("http://" + domain + ":" + REVERSE_PROXY_PORT);

        } catch (Exception e) {
            log.error("Deployment failed for project {}", projectId, e);
            client.pods()
                    .inNamespace(NAMESPACE)
                    .withName(podName)
                    .edit(p -> {
                        p.getMetadata().getLabels().put(POOL_LABEL, IDLE);
                        p.getMetadata().getLabels().remove(PROJECT_LABEL);
                        return p;
                    });
            throw new RuntimeException("Deployment failed.", e);
        }
    }

    private void registerRoute(String domain, Pod pod) {
        String ip = pod.getStatus().getPodIP();
        if (ip == null) {
            throw new RuntimeException("Runner pod has no assigned IP address.");
        }

        redisTemplate.opsForValue().set(
                "route:" + domain,
                ip + ":5173",
                6,
                TimeUnit.HOURS
        );
        log.info("Registered route {} -> {}:5173", domain, ip);
    }

    private void execCommand(String podName, String container, String... command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CompletableFuture<Void> done = new CompletableFuture<>();

        String lastSegment = command[command.length - 1];
        boolean isBackground = lastSegment.trim().endsWith("&");

        ExecWatch watch = client.pods()
                .inNamespace(NAMESPACE)
                .withName(podName)
                .inContainer(container)
                .writingOutput(out)
                .writingError(err)
                .usingListener(new ExecListener() {
                    @Override
                    public void onClose(int code, String reason) {
                        if (code == 0 || code == 1000) {
                            done.complete(null);
                        } else {
                            String errorMsg = String.format("Stream closed with code %d. Reason: %s. Stderr: %s",
                                    code, reason, err.toString());
                            done.completeExceptionally(new RuntimeException(errorMsg));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, Response response) {
                        done.completeExceptionally(t);
                    }
                })
                .exec(command);

        try {
            if (isBackground) {
                Thread.sleep(1500);
                watch.close();
            } else {
                try {
                    done.get(3, TimeUnit.MINUTES);
                } finally {
                    watch.close();
                }
            }
        } catch (Exception e) {
            log.error("Command execution error context. Stderr payload: {}", err);
            throw new RuntimeException("Error running command in container: " + err, e);
        }
    }

    private Pod findActivePod(Long projectId) {
        return client.pods()
                .inNamespace(NAMESPACE)
                .withLabel(PROJECT_LABEL, projectId.toString())
                .withLabel(POOL_LABEL, BUSY)
                .list()
                .getItems()
                .stream()
                .filter(p -> "Running".equalsIgnoreCase(p.getStatus().getPhase()))
                .findFirst()
                .orElse(null);
    }
}