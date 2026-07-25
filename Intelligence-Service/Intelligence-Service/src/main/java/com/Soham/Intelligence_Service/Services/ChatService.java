package com.Soham.Intelligence_Service.Services;







import com.Soham.Intelligence_Service.DTOs.Chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
