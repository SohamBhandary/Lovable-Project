package com.Soham.Intelligence_Service.Services;

public interface UsageServie {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
