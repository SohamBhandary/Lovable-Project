package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Lovable_Project.Enums.SubcriptionStatus;

import java.time.Instant;

public interface SubcriptionService {
    SubcriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String subscriptionId, SubcriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String subscriptionId);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);

    boolean canCreateNewProject();
}
