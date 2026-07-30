package com.Soham.Lovable_Project.DTOs.Subcription;

import java.time.Instant;

public record SubcriptionResponse(
        PlanReponse plan,
        String status,
        Instant currentPeriodEnd,
        Long tokenUsedThisCycle
) {
}
