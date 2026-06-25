package com.Soham.Lovable_Project.DTOs.Project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;



import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "Name is required") String name
) {}
