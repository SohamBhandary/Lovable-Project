package com.Soham.Lovable_Project.Tools;

import com.Soham.Lovable_Project.Services.ProjectFIleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CodeGenerationTools {

    private final ProjectFIleService projectFileService;
    private final Long projectId;

    @Tool(name = "read_files",
            description = "Read the content of files. Only input the file names present inside the FILE_TREE. DO NOT input any path which is not present under the FILE_TREE.")
    public String readFiles( // 1. Changed return type from List<String> to String
                             @ToolParam(description = "List of relative paths (e.g., ['src/App.tsx'])")
                             List<String> paths
    ) {
        StringBuilder resultBuilder = new StringBuilder();

        for (String path : paths) {
            String cleanPath = path.startsWith("/") ? path.substring(1) : path;

            log.info("Requested file: {}", cleanPath);

            try {
                var fileResponse = projectFileService.getFileContent(projectId, cleanPath);
                String content = (fileResponse != null) ? fileResponse.content() : null;

                // 2. Fallback protection for empty/null files so the LLM doesn't get a blank response
                if (content == null || content.trim().isEmpty()) {
                    content = "// File is empty or has no content yet.";
                }

                resultBuilder.append(String.format(
                        "--- START OF FILE: %s ---\n%s\n--- END OF FILE ---\n\n",
                        cleanPath, content
                ));

            } catch (Exception e) {
                log.error("Error reading file path {}: {}", cleanPath, e.getMessage());
                resultBuilder.append(String.format(
                        "--- START OF FILE: %s ---\nError reading file: %s\n--- END OF FILE ---\n\n",
                        cleanPath, e.getMessage()
                ));
            }
        }

        // 3. Return a single flat text block back to Spring AI
        return resultBuilder.toString();
    }
}