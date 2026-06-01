package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PlanLimitResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.UsageTodayResponse;
import com.Soham.Lovable_Project.Services.UsageServie;
import com.Soham.Lovable_Project.Services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImple implements UsageServie {

    @Override
    public UsageTodayResponse getTodayUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubcriptionLimitsofUser(Long userId) {
        return null;
    }
}
