package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutRequest;
import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PortalResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Lovable_Project.Entities.Plan;
import com.Soham.Lovable_Project.Entities.Subcription;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Enums.SubcriptionStatus;
import com.Soham.Lovable_Project.Error.ResourceNotFoundException;
import com.Soham.Lovable_Project.Mapper.SubcriptionMapper;
import com.Soham.Lovable_Project.Repositories.PlanRepository;
import com.Soham.Lovable_Project.Repositories.SubcriptionRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.SubcriptionService;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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


    public SubcriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription = subcriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubcriptionStatus.ACTIVE, SubcriptionStatus.PAST_DUE,
                SubcriptionStatus.TRAILING
        )).orElse(
                new Subscription()
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

      Subcription subcription=Subcription.builder().
              user(user).plan(plan).stripeSubscriptionId(subscriptionId)
              .status(SubcriptionStatus.INCOMPLETE).

              build();

        subcriptionRepository.save(subcription);
    }



    @Override
    public void updateSubscription(String subscriptionId, SubcriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {



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
