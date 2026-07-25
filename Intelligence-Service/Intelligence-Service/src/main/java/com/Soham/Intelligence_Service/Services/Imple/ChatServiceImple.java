package com.Soham.Intelligence_Service.Services.Imple;


import com.Soham.Common_Lib.Security.AuthUtil;
import com.Soham.Intelligence_Service.DTOs.Chat.ChatResponse;
import com.Soham.Intelligence_Service.Entities.ChatMessage;
import com.Soham.Intelligence_Service.Entities.ChatSession;
import com.Soham.Intelligence_Service.Entities.ChatSessionId;
import com.Soham.Intelligence_Service.Mapper.ChatMapper;
import com.Soham.Intelligence_Service.Repository.ChatMessageRepository;
import com.Soham.Intelligence_Service.Repository.ChatSessionRepository;
import com.Soham.Intelligence_Service.Services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImple implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final AuthUtil authUtil;
    private final ChatMapper chatMapper;



    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId, userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessage(chatMessageList);
    }
}
