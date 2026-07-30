package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PlanLimitResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PlanReponse;
import com.Soham.Lovable_Project.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.UsageTodayResponse;
import com.Soham.Lovable_Project.Entities.UsageLog;
import com.Soham.Lovable_Project.Repositories.UsageLogRepository;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.SubcriptionService;
import com.Soham.Lovable_Project.Services.UsageServie;
import com.Soham.Lovable_Project.Services.UserService;
import com.stripe.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsageServiceImple implements UsageServie {

    private final UsageLogRepository usageLogRepository;
    private final AuthUtil authUtil;
    private final SubcriptionService subscriptionService;

    @Override
    public void recordTokenUsage(Long userId, int actualTokens) {
        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today).
                orElseGet(() -> createNewDailyLog(userId, today));

        todayLog.setTokensUsed(todayLog.getTokensUsed() + actualTokens);
        usageLogRepository.save(todayLog);
    }

    @Override
    public void checkDailyTokensUsage() {
        Long userId = authUtil.getCurrentUserId();
        SubcriptionResponse subscriptionResponse = subscriptionService.getCurrentSubscription();;
        PlanReponse plan = subscriptionResponse.plan();

        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today).
                orElseGet(() -> createNewDailyLog(userId, today));

        if(plan.unlimitedAi()) return;

        int currentUsage = todayLog.getTokensUsed();
        int limit = plan.maxTokensPerDay();

        if(currentUsage >=  limit) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Daily limit reached, Upgrade now");
        }

    }

    private UsageLog createNewDailyLog(Long userId, LocalDate date) {
        UsageLog newLog = UsageLog.builder()
                .userId(userId)
                .date(date)
                .tokensUsed(0)
                .build();
        return usageLogRepository.save(newLog);
    }


}
