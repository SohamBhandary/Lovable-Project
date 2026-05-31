package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Subcription.*;
import com.Soham.Lovable_Project.Services.PlanService;
import com.Soham.Lovable_Project.Services.SubcriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class BillingController {
    private final PlanService planService;
    private final SubcriptionService subcriptionService;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanReponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getActivePlans());
    }
    @GetMapping("/api/me/subcription")
    public ResponseEntity<SubcriptionResponse> getMySubcription(){
        Long userId=1L;
        return ResponseEntity.ok(subcriptionService.getCurrentSubcription(userId));
    }
    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResposne(   @RequestBody CheckoutRequest checkoutRequest){
        Long userId=1L;
        return ResponseEntity.ok(subcriptionService.createCheckoutSeesionUrl(checkoutRequest,userId));


    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId=1L;
        return ResponseEntity.ok(subcriptionService.openCustomerPortal(userId));

    }

}
