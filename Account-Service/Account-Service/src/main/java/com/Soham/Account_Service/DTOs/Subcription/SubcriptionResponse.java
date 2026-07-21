package com.Soham.Account_Service.DTOs.Subcription;



import com.Soham.Common_Lib.DTOs.PlanDto;

import java.time.Instant;

public record SubcriptionResponse(
        PlanDto plan,
        String status,
        Instant currentPeriodEnd,
        Long tokenUsedThisCycle
) {
}
