package com.Soham.Account_Service.Services;

import com.Soham.Account_Service.DTOs.Auth.AuthResponse;
import com.Soham.Account_Service.DTOs.Auth.LoginRequest;
import com.Soham.Account_Service.DTOs.Auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}

