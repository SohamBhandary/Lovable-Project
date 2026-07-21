package com.Soham.Account_Service.Services;


import com.Soham.Account_Service.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Common_Lib.DTOs.PlanDto;
import com.Soham.Common_Lib.Enums.SubcriptionStatus;

import java.time.Instant;

public interface SubcriptionService {
    SubcriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String subscriptionId, SubcriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String subscriptionId);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);

   PlanDto getCurrentSubscribedPlanByUser();


}