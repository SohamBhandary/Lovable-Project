package com.Soham.Account_Service.Services;

import com.Soham.Account_Service.DTOs.Subcription.CheckoutRequest;
import com.Soham.Account_Service.DTOs.Subcription.CheckoutResponse;
import com.Soham.Account_Service.DTOs.Subcription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSeesionUrl(CheckoutRequest checkoutRequest);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
