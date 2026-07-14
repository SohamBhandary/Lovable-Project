package com.Soham.Lovable_Project.DTOs.Chat;

import com.Soham.Lovable_Project.Enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
