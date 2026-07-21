package com.Soham.Workspace_Service.DTOs.Project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "Name is required") String name
) {}
