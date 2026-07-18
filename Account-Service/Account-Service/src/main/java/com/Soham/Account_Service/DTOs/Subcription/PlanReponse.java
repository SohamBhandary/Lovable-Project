package com.Soham.Account_Service.DTOs.Subcription;

public record PlanReponse(
        Long id,
        String name,
        String stripePriceId,
        Integer maxProjects,
        Integer maxTokensPerDay,
        Integer maxPreviews,
        Boolean unlimitedAi,
        Boolean active

) {
}
