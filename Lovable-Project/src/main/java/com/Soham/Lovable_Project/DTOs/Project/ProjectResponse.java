package com.Soham.Lovable_Project.DTOs.Project;

import com.Soham.Lovable_Project.DTOs.Auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id, String name, Instant createdAt, Instant updatedAt,
        UserProfileResponse owner
) {
}
