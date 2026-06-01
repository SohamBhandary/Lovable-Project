package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Auth.AuthResponse;
import com.Soham.Lovable_Project.DTOs.Auth.LoginRequest;
import com.Soham.Lovable_Project.DTOs.Auth.SignupRequest;
import com.Soham.Lovable_Project.Services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
