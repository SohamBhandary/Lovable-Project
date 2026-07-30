package com.Soham.Lovable_Project.DTOs.Project;

import com.Soham.Lovable_Project.Enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(Long id, String name, Instant createdAt, Instant updatedAt, ProjectRole role) {
}
