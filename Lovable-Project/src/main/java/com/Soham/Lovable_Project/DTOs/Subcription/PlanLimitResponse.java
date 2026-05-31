package com.Soham.Lovable_Project.DTOs.Subcription;

public record PlanLimitResponse(
        String planName,
        int maxTokensPerDay,
        int maxProjects,
        boolean unlimitedAi

) {
}
