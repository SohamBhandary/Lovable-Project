package com.Soham.Account_Service.DTOs.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@Email @NotBlank String username, @Size(min = 4,max=30) String password) {
}
