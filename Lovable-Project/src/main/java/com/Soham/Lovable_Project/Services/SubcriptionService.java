package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutRequest;
import com.Soham.Lovable_Project.DTOs.Subcription.CheckoutResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.PortalResponse;
import com.Soham.Lovable_Project.DTOs.Subcription.SubcriptionResponse;
import org.jspecify.annotations.Nullable;

public interface SubcriptionService {
    SubcriptionResponse getCurrentSubcription(Long userId);

    CheckoutResponse createCheckoutSeesionUrl(CheckoutRequest checkoutRequest, Long userId);

     PortalResponse openCustomerPortal(Long userId);
}
