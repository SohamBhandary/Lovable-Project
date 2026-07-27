package com.Soham.Intelligence_Service.DTOs.Chat;


import com.Soham.Common_Lib.Enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
