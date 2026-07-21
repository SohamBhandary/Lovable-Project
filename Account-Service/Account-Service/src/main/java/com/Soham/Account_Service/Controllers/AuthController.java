package com.Soham.Account_Service.Controllers;


import com.Soham.Account_Service.DTOs.Auth.AuthResponse;
import com.Soham.Account_Service.DTOs.Auth.LoginRequest;
import com.Soham.Account_Service.DTOs.Auth.SignupRequest;
import com.Soham.Account_Service.DTOs.Auth.UserProfileResponse;
import com.Soham.Account_Service.Services.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;
//    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));

    }

//    @GetMapping("/login")
//    public ResponseEntity<UserProfileResponse> getProfile(LoginRequest request){
//        Long userId= 1L;
//        return ResponseEntity.ok(userService.getProfile(userId));
//    }


}
