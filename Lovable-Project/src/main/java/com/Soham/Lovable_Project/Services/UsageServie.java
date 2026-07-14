package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanLimitResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageServie {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
