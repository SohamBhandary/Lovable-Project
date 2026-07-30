package com.Soham.Lovable_Project.DTOs.Chat;

import com.Soham.Lovable_Project.Entities.ChatEvent;
import com.Soham.Lovable_Project.Entities.ChatSession;
import com.Soham.Lovable_Project.Enums.MessageRole;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        MessageRole role,
        List<ChatEventResponse> events,
        String content,
        Integer tokensUsed,
        Instant createdAt
) {
}
