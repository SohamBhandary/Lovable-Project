package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanLimitResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.UsageTodayResponse;
import com.Soham.Lovable_Project.Services.UsageServie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {
    private final UsageServie usageServie;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage(){
        Long userId=1L;
        return ResponseEntity.ok(usageServie.getTodayUsage(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitResponse> getPlanLimits(){
        Long userId=1L;
        return ResponseEntity.ok(usageServie.getCurrentSubcriptionLimitsofUser(userId));
    }

}
