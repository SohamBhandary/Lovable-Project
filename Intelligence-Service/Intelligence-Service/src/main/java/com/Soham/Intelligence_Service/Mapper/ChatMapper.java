package com.Soham.Intelligence_Service.Mapper;



import com.Soham.Intelligence_Service.DTOs.Chat.ChatResponse;
import com.Soham.Intelligence_Service.Entities.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}