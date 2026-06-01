package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanLimitResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageServie {
   UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubcriptionLimitsofUser(Long userId);
}
