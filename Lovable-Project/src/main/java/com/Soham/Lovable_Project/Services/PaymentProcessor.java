package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutRequest;
import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSeesionUrl(CheckoutRequest checkoutRequest);

    PortalResponse openCustomerPortal(Long userId);

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
