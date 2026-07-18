package com.Soham.Account_Service.DTOs.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SignupRequest(@Email String username, @Size(min = 1,max = 50) String name, @Size(min = 4) String password ) {
}
