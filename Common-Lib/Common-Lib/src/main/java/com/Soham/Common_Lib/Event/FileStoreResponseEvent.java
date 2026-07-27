package com.Soham.Common_Lib.Event;

import lombok.Builder;

@Builder
public record FileStoreResponseEvent(
        String sagaId,
        boolean success,
        String errorMessage,
        Long projectId
) {}