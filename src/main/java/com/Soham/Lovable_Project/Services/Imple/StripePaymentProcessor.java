package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutRequest;
import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PortalResponse;
import com.Soham.Lovable_Project.Entities.Plan;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Enums.SubcriptionStatus;
import com.Soham.Lovable_Project.Error.BadRequestException;
import com.Soham.Lovable_Project.Error.ResourceNotFoundException;
import com.Soham.Lovable_Project.Repositories.PlanRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.PaymentProcessor;
import com.Soham.Lovable_Project.Services.SubcriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

import static com.Soham.Lovable_Project.Enums.SubcriptionStatus.TRAILING;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcessor implements PaymentProcessor {

    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubcriptionService subcriptionService;

    @Value("${client.url}")
    private String frontend;

    @Override
    public CheckoutResponse createCheckoutSeesionUrl(CheckoutRequest request) {

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan", request.planId().toString()));

        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", userId.toString()));
        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                                .build()
                                )
                                .build()
                )
                .setSuccessUrl(frontend + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontend + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());

        try {

            String stripeCustomerId = user.getStripeCustomerId();

            if (stripeCustomerId == null || stripeCustomerId.isBlank()) {
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }

            Session session = Session.create(params.build());



            return new CheckoutResponse(session.getUrl());

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe Checkout Session", e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {
        Long userid=authUtil.getCurrentUserId();
       User user=getUser(userid);
       String stripeCustomerId=user.getStripeCustomerId();
       if(stripeCustomerId==null || stripeCustomerId.isEmpty()){
           throw new BadRequestException("User doest not have a Stripe Customer ID, userID:="+userid);

       }

        try {
            var portalSession = com.stripe.model.billingportal.Session.create(
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(stripeCustomerId)
                            .setReturnUrl(frontend)
                            .build()
            );

            log.info("Portal Session ID: {}", portalSession.getId());
            log.info("Portal URL: {}", portalSession.getUrl());

            return new PortalResponse(portalSession.getUrl());

        } catch (StripeException e) {
            log.error("Stripe Error", e);
            throw new RuntimeException(e);
        }



    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.debug("Handling stripe event: {}", type);

        switch (type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metadata); // one-time, on checkout completed
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject); // when user cancels, upgrades or any updates
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject); // when subscription ends, revoke the access
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject); // when invoice is paid
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject); // when invoice is not paid, mark as PAST_DUE
            default -> log.debug("Ignoring the event: {}", type);
        }
    }

    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metadata) {
        if(session == null) {
            log.error("session object was null");
            return;
        }

        Long userId = Long.parseLong(metadata.get("user_id"));
        Long planId = Long.parseLong(metadata.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();

        User user = getUser(userId);
        if(user.getStripeCustomerId() == null) {
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }

        subcriptionService.activateSubscription(userId, planId, subscriptionId, customerId);
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription) {
        if (subscription == null) {
            log.error("subscription object was null inside handleCustomerSubscriptionUpdated");
            return;
        }

        SubcriptionStatus status = mapStripeStatusToEnum(subscription.getStatus());
        if (status == null) {
            log.warn("Unknown status '{}' for subscription {}", subscription.getStatus(), subscription.getId());
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        subcriptionService.updateSubscription(
                subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId
        );

    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
        if (subscription == null) {
            log.error("subscription object was null inside handleCustomerSubscriptionDeleted");
            return;
        }
        subcriptionService.cancelSubscription(subscription.getId());
    }

    private void handleInvoicePaid(Invoice invoice) {
        String subId = extractSubscriptionId(invoice);
        if(subId == null) return;

        try {
            Subscription subscription = Subscription.retrieve(subId); //sdk calling the Stripe server
            var item = subscription.getItems().getData().get(0);

            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            subcriptionService.renewSubscriptionPeriod(
                    subId,
                    periodStart,
                    periodEnd
            );

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleInvoicePaymentFailed(Invoice invoice) {
        String subId = extractSubscriptionId(invoice);
        if(subId == null) return;

        subcriptionService.markSubscriptionPastDue(subId);
    }


    /// // Utility Methods

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("user", userId.toString()));
    }

    private SubcriptionStatus mapStripeStatusToEnum(String status) {
        return switch (status) {
            case "active" -> SubcriptionStatus.ACTIVE;
            case "trialing" -> TRAILING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> TRAILING.PAST_DUE;
            case "canceled" -> SubcriptionStatus.CANCELED;
            case "incomplete" -> SubcriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe status: {}", status);
                yield null;
            }
        };
    }

    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price) {
        if (price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if (parent == null) return null;

        var subDetails = parent.getSubscriptionDetails();
        if (subDetails == null) return null;

        return subDetails.getSubscription();
    }
}