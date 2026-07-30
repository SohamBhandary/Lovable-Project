package com.Soham.Lovable_Project.LLM.Advisor;

import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.Services.ProjectFIleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileTreeContextAdvisor implements StreamAdvisor {

    private final ProjectFIleService projectFileService;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain streamAdvisorChain) {
        Map<String, Object> context = request.context();
        Long projectId = Long.parseLong(context.getOrDefault("projectId", 0).toString());

        ChatClientRequest augmentedChatClientRequest = augmentRequestWithFileTree(request, projectId);
        return streamAdvisorChain.nextStream(augmentedChatClientRequest);
    }

    private ChatClientRequest augmentRequestWithFileTree(ChatClientRequest request, Long projectId) {
        List<Message> incomingMessages = request.prompt().getInstructions();
        List<Message> allMessages = new ArrayList<>();

        // 1. Maintain ALL historical messages intact (System prompts, User turns, Assistant turns)
        allMessages.addAll(incomingMessages);

        // 2. Fetch the flat data structure list and wrap as context payload metadata
        List<FileNode> fileTree = projectFileService.getFileTree(projectId).files();
        String fileTreeContext = "\n\n---- AVAILABLE PROJECT WORKSPACE FILE_TREE ----\n" + fileTree.toString();

        // 3. Inject the context window as a supporting system baseline marker
        allMessages.add(new SystemMessage(fileTreeContext));

        return request
                .mutate()
                .prompt(new Prompt(allMessages, request.prompt().getOptions()))
                .build();
    }

    @Override
    public String getName() {
        return "FileTreeContextAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}