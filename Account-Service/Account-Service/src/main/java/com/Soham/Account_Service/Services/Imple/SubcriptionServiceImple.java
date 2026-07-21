package com.Soham.Account_Service.Services.Imple;

import com.Soham.Account_Service.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Account_Service.Entities.Plan;
import com.Soham.Account_Service.Entities.Subcription;
import com.Soham.Account_Service.Entities.User;
import com.Soham.Account_Service.Mapper.SubcriptionMapper;
import com.Soham.Account_Service.Repositories.PlanRepository;
import com.Soham.Account_Service.Repositories.SubcriptionRepository;
import com.Soham.Account_Service.Repositories.UserRepository;
import com.Soham.Account_Service.Services.SubcriptionService;
import com.Soham.Common_Lib.DTOs.PlanDto;
import com.Soham.Common_Lib.Enums.SubcriptionStatus;
import com.Soham.Common_Lib.Error.ResourceNotFoundException;
import com.Soham.Common_Lib.Security.AuthUtil;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubcriptionServiceImple implements SubcriptionService {
    private final AuthUtil authUtil;
    private final SubcriptionRepository subcriptionRepository;
    private final SubcriptionMapper subcriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;


    private final Integer FREE_TIER_PROJECTS_ALLOWED = 100;


    public SubcriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription = subcriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubcriptionStatus.ACTIVE, SubcriptionStatus.PAST_DUE,
                SubcriptionStatus.TRAILING
        )).orElse(
                new Subcription()
        );

        return subcriptionMapper.toSubcriptionResponse(currentSubscription);
    }

    public SubcriptionResponse getCurrentSubcription(Long userId) {
        return null;
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {

        boolean exists = subcriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subcription subcription = Subcription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .stripeCustomerId(customerId) // ✅ FIXED: Use the parameter directly!
                .status(SubcriptionStatus.ACTIVE) // Tip: Change to ACTIVE if they successfully completed payment
                .build();

        subcriptionRepository.save(subcription);

        subcriptionRepository.save(subcription);
    }



    @Override
    @Transactional
    public void updateSubscription(String subscriptionId, SubcriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subcription subcription=getSubcription(subscriptionId);
        Boolean subcriptionHasBennUpdated=false;
        if(status!=null && status!=subcription.getStatus()){
            subcription.setStatus(status);
            subcriptionHasBennUpdated=true;

        }
        if(periodStart!=null && !periodStart.equals(subcription.getCurrentPeriodStart())){
            subcription.setCurrentPeriodStart(periodStart);
            subcriptionHasBennUpdated=true;

        }
        if(periodEnd!=null && !periodEnd.equals(subcription.getCancelAtPeriodEnd())){
            subcription.setCurrentPeriodEnd(periodEnd);
            subcriptionHasBennUpdated=true;

        }

        if(cancelAtPeriodEnd!=null && cancelAtPeriodEnd!=subcription.getCancelAtPeriodEnd()){
            subcription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            subcriptionHasBennUpdated=true;
        }

        if(planId!=null && !planId.equals(subcription.getPlan().getId())){
            Plan newPlan=getPlan(planId);
            subcription.setPlan(newPlan);

            subcriptionHasBennUpdated=true;

        }
        if(subcriptionHasBennUpdated){
            log.debug("Subcription has been updated :{} ",subscriptionId);

        }





    }

    @Override
    public void cancelSubscription(String subscriptionId) {
        Subcription subcription=getSubcription(subscriptionId);
        subcription.setStatus(SubcriptionStatus.CANCELED);
        subcriptionRepository.save(subcription);

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subcription subscription = getSubcription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);

        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubcriptionStatus.PAST_DUE || subscription.getStatus() == SubcriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubcriptionStatus.ACTIVE);
        }

        subcriptionRepository.save(subscription);
    }



    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {

        Subcription subcription=getSubcription(gatewaySubscriptionId);
        if(subcription.getStatus()==SubcriptionStatus.PAST_DUE){
            log.debug("Subcription is already past due, gatewaySubscriptionId:{} ",gatewaySubscriptionId);
            return;
        }
        subcription.setStatus(SubcriptionStatus.PAST_DUE);
        subcriptionRepository.save(subcription);

    }

    @Override
    public PlanDto getCurrentSubscribedPlanByUser() {
        SubcriptionResponse subscriptionResponse = getCurrentSubscription();
        return subscriptionResponse.plan();
    }


    ///  Utility methods

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));

    }

    private Subcription getSubcription(String gatewaySubscriptionId) {
        return subcriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(() ->
                new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }
}
