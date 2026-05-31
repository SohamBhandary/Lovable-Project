package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.AuthResponse;
import com.Soham.Lovable_Project.DTOs.LoginRequest;
import com.Soham.Lovable_Project.DTOs.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {
     AuthResponse signup(SignupRequest request);

     AuthResponse login(LoginRequest request);
}
