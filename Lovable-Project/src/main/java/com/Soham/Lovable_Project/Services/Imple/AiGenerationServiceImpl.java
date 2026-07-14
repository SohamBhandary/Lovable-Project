package com.Soham.Lovable_Project.Services.Imple;


import com.Soham.Lovable_Project.DTOs.Chat.StreamResponse;
import com.Soham.Lovable_Project.Entities.*;
import com.Soham.Lovable_Project.Enums.ChatEventType;
import com.Soham.Lovable_Project.Enums.MessageRole;
import com.Soham.Lovable_Project.Error.ResourceNotFoundException;
import com.Soham.Lovable_Project.LLM.Advisor.FileTreeContextAdvisor;
import com.Soham.Lovable_Project.LLM.LlmResponseParser;
import com.Soham.Lovable_Project.LLM.PromptUtils;
import com.Soham.Lovable_Project.Repositories.*;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.AIGenerationService;
import com.Soham.Lovable_Project.Services.ProjectFIleService;
import com.Soham.Lovable_Project.Services.UsageServie;
import com.Soham.Lovable_Project.Tools.CodeGenerationTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AIGenerationService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final ProjectFIleService projectFileService;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final ChatSessionRepository chatSessionRepository;
    private final ProjectRepository projectRepository;
    private final LlmResponseParser llmResponseParser;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository chatEventRepository;
    private final UsageLogRepository usageLogRepository;
    private final UsageServie usageServie;

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);


    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<StreamResponse> streamResponse(String userMessage, Long projectId) {

        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();
        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);

        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);
        AtomicReference<Usage> usageRef = new AtomicReference<>();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorParams);
                    advisorSpec.advisors(fileTreeContextAdvisor);
                })
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    if (response != null && response.getMetadata() != null && response.getMetadata().getUsage() != null) {
                        usageRef.set(response.getMetadata().getUsage());
                    }

                    if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
                        return;
                    }

                    String content = response.getResult().getOutput().getText();
                    if (content != null && !content.isBlank()) {
                        if (endTime.get() == 0) {
                            endTime.set(System.currentTimeMillis());
                        }
                        fullResponseBuffer.append(content);
                    }
                })
                .map(response -> {
                    if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
                        // Send an explicit pulse/metadata token instead of an empty block to keep connection fresh
                        return new StreamResponse("");
                    }

                    String text = response.getResult().getOutput().getText();
                    return new StreamResponse(text == null ? "" : text);
                })
                // REMOVED THE BRITTLE .filter() operator that was devouring the tool execution transition frames

                // Explicitly publish on an elastic scheduler to decouple network writes from Tool database lookups
                .publishOn(Schedulers.boundedElastic())

                .doOnComplete(() -> {
                    // Keep this async execution decoupled completely from the stream pipeline return window
                    Mono.fromRunnable(() -> {
                                parseAndSaveFiles(fullResponseBuffer.toString(), projectId);
                                long duration = endTime.get() == 0 ? 0 : (endTime.get() - startTime.get()) / 1000;
                                finalizeChats(userMessage, chatSession, fullResponseBuffer.toString(), duration, usageRef.get(), userId);
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();
                })
                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId, error));
    }

    /**
     * Extracts <file path="...">...</file> blocks using regex patterns
     * and saves them directly via ProjectFileService.
     */
    private void parseAndSaveFiles(String fullText, Long projectId) {
        if (fullText == null || fullText.isEmpty()) return;

        Matcher matcher = FILE_TAG_PATTERN.matcher(fullText);
        while (matcher.find()) {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2);

            try {
                log.info("Auto-saving generated code changes for file: {} in project: {}", filePath, projectId);
                projectFileService.saveFile(projectId, filePath, fileContent);
            } catch (Exception e) {
                log.error("Failed to auto-save file: {} via stream completion", filePath, e);
            }
        }
    }

    private void finalizeChats(String userMessage, ChatSession chatSession, String fullText, Long duration, Usage usage,Long  userId) {
        Long projectId = chatSession.getProject().getId();
        if(usage != null) {
            int totalTokens = usage.getTotalTokens();
            usageServie.recordTokenUsage(chatSession.getUser().getId(), totalTokens);
        }
        // Save the User message
        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .role(MessageRole.USER)
                        .content(userMessage)
                        .tokensUsed(usage.getPromptTokens())
                        .build()
        );

        ChatMessage assistantChatMessage = ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .content(fullText) // Save the real text instead of a placeholder text block
                .chatSession(chatSession)
                .build();

        assistantChatMessage = chatMessageRepository.save(assistantChatMessage);

        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistantChatMessage);
        chatEventList.addFirst(ChatEvent.builder()
                .type(ChatEventType.THOUGHT)
                .chatMessage(assistantChatMessage)
                .content("Thought for "+duration+"s")
                .sequenceOrder(0)
                .build());

        chatEventList.stream()
                .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
                .forEach(e -> projectFileService.saveFile(projectId, e.getFilePath(), e.getContent()));

        chatEventRepository.saveAll(chatEventList);
    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession == null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();

            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}