package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Chat.StreamResponse;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.stream.Stream;

public interface AIGenerationService {
   Flux<StreamResponse> streamResponse(String message, Long projectId);
}
