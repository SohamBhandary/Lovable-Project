package com.Soham.Account_Service.Controllers;


import com.Soham.Account_Service.Mapper.UserMapper;
import com.Soham.Account_Service.Repositories.UserRepository;
import com.Soham.Account_Service.Services.SubcriptionService;
import com.Soham.Common_Lib.DTOs.PlanDto;
import com.Soham.Common_Lib.DTOs.UserDto;
import com.Soham.Common_Lib.Error.ResourceNotFoundException;
import com.stripe.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/internal/v1")
@RequiredArgsConstructor
public class InternalAccountController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubcriptionService subscriptionService;

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }

    @GetMapping("/users/by-email")
    public Optional<UserDto> getUserByEmail(@RequestParam String email) {
        return userRepository.findByUsernameIgnoreCase(email)
                .map(userMapper::toUserDto);
    }

    @GetMapping("/billing/current-plan")
    public PlanDto getCurrentSubscribedPlan() {
        return subscriptionService.getCurrentSubscribedPlanByUser();
    }
}