package com.Soham.Lovable_Project.DTOs.Subcription;

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
