package com.Soham.Account_Service.DTOs.Subcription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProjects,
        Boolean unlimitedAi

) {
}
