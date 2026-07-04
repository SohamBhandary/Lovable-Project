package com.Soham.Lovable_Project.Services;

import reactor.core.publisher.Flux;

import java.util.Optional;

public interface AIGenerationService {
   Flux<String> streamResponse(String message, Long aLong);
}
