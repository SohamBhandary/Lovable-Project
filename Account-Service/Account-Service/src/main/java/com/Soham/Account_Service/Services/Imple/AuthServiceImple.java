package com.Soham.Account_Service.Services.Imple;

import com.Soham.Account_Service.DTOs.Auth.AuthResponse;
import com.Soham.Account_Service.DTOs.Auth.LoginRequest;
import com.Soham.Account_Service.DTOs.Auth.SignupRequest;
import com.Soham.Account_Service.Entities.User;
import com.Soham.Account_Service.Mapper.UserMapper;
import com.Soham.Account_Service.Repositories.UserRepository;
import com.Soham.Account_Service.Services.AuthService;
import com.Soham.Common_Lib.Error.BadRequestException;
import com.Soham.Common_Lib.Security.AuthUtil;
import com.Soham.Common_Lib.Security.JWTUserprincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImple implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("User already exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        // FIX: Match the exact 5 parameters using explicit types
        JWTUserprincipal jwtUserPrincipal = new JWTUserprincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );

        String token = authUtil.generateAccessToken(jwtUserPrincipal);
        return new AuthResponse(token, userMapper.toUserProfileResponse(jwtUserPrincipal));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        JWTUserprincipal user1 = (JWTUserprincipal) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user1);

        User user = userRepository.findByUsername(user1.getUsername())
                .orElseThrow(() -> new BadRequestException("User configuration out of sync"));

        return new AuthResponse(token, userMapper.toUserProfileResponse(user1));
    }
}