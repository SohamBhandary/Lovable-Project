package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Chat.ChatResponse;
import com.Soham.Lovable_Project.Entities.ChatMessage;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}