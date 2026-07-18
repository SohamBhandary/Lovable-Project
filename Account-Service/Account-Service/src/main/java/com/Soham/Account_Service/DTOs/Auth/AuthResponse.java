package com.Soham.Account_Service.DTOs.Auth;

public record AuthResponse(String token, UserProfileResponse userProfileResponse) {
}
