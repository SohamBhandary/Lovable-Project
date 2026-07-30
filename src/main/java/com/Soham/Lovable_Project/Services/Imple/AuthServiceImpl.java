package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Auth.AuthResponse;
import com.Soham.Lovable_Project.DTOs.Auth.LoginRequest;
import com.Soham.Lovable_Project.DTOs.Auth.SignupRequest;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Error.BadRequestException;
import com.Soham.Lovable_Project.Mapper.UserMapper;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)

public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;





    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("User already exists with username: "+request.username());
        });
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user=userRepository.save(user);
        String token= authUtil.generateAccessToken(user);


        return new AuthResponse(token,userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication= authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );

        User user = (User) authentication.getPrincipal();

        String token=authUtil.generateAccessToken(user);

        return new AuthResponse(token,userMapper.toUserProfileResponse(user));





    }
}
