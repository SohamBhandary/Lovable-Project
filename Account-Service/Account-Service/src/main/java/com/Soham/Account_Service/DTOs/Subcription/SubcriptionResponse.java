package com.Soham.Account_Service.DTOs.Subcription;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanReponse;

import java.time.Instant;

public record SubcriptionResponse(
        PlanReponse plan,
        String status,
        Instant currentPeriodEnd,
        Long tokenUsedThisCycle
) {
}
