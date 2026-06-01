package com.Soham.Lovable_Project.DTOs.Subcription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProjects,
        Boolean unlimitedAi

) {
}
