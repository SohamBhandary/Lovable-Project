package com.Soham.Lovable_Project.Services;



import com.Soham.Lovable_Project.DTOs.Chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
