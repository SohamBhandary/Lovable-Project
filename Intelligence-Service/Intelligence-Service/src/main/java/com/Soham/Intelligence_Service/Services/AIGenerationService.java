package com.Soham.Intelligence_Service.Services;


import com.Soham.Intelligence_Service.DTOs.Chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AIGenerationService {
   Flux<StreamResponse> streamResponse(String message, Long projectId);
}
