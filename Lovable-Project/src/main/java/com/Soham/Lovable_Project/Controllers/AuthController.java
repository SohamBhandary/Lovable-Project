package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Auth.AuthResponse;
import com.Soham.Lovable_Project.DTOs.Auth.LoginRequest;
import com.Soham.Lovable_Project.DTOs.Auth.SignupRequest;
import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;
import com.Soham.Lovable_Project.Services.AuthService;
import com.Soham.Lovable_Project.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(SignupRequest request){
        return  ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(LoginRequest request){
        return  ResponseEntity.ok(authService.login(request));

    }

    @GetMapping("/login")
    public ResponseEntity<UserProfileResponse> getProfile(LoginRequest request){
        Long userId= 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }


}
