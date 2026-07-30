package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Auth.AuthResponse;
import com.Soham.Lovable_Project.DTOs.Auth.LoginRequest;
import com.Soham.Lovable_Project.DTOs.Auth.SignupRequest;

public interface AuthService {
     AuthResponse signup(SignupRequest request);

     AuthResponse login(LoginRequest request);
}
