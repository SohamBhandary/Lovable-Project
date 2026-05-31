package com.Soham.Lovable_Project.DTOs.Project;

import java.time.Instant;

public record ProjectSummaryResponse(Long id, String name, Instant createdAt,Instant updatedAt) {
}
