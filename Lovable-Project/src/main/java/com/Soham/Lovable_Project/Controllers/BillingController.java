package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Subcription.*;
import com.Soham.Lovable_Project.Services.PaymentProcessor;
import com.Soham.Lovable_Project.Services.PlanService;
import com.Soham.Lovable_Project.Services.SubcriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j

public class BillingController {
    private final PlanService planService;
    private final SubcriptionService subcriptionService;
    private final PaymentProcessor paymentProcessor;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanReponse>> getAllPlans(){


        return ResponseEntity.ok(planService.getActivePlans());
    }
    @GetMapping("/api/me/subcription")
    public ResponseEntity<SubcriptionResponse> getMySubcription(){
        Long userId=1L;
        return ResponseEntity.ok(subcriptionService.getCurrentSubscription());
    }
    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResposne(   @RequestBody CheckoutRequest checkoutRequest){

        return ResponseEntity.ok(paymentProcessor.createCheckoutSeesionUrl(checkoutRequest));




    }

    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){

//        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
        return null;

    }

    @PostMapping("/webhooks/payment")
    public ResponseEntity<String> handlePaymentWebhooks(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            if (deserializer.getObject().isPresent()) { // happy case
                stripeObject = deserializer.getObject().get();
            } else {
                // Fallback: Deserialize from raw JSON
                try {
                    stripeObject = deserializer.deserializeUnsafe();
                    if (stripeObject == null) {
                        log.warn("Failed to deserialize webhook object for event: {}", event.getType());
                        return ResponseEntity.ok().build();
                    }
                } catch (Exception e) {
                    log.error("Unsafe deserialization failed for event {}: {}", event.getType(), e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization failed");
                }
            }

            // Now extract metadata only if it's a Checkout Session
            Map<String, String> metadata = new HashMap<>();
            if (stripeObject instanceof Session session) {
                metadata = session.getMetadata();
            }

            // Pass to your processor
            paymentProcessor.handleWebhookEvent(event.getType(), stripeObject, metadata);
            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

    }

}
