package com.Soham.Workspace_Service.DTOs.Project;



import java.time.Instant;

public record ProjectResponse(
        Long id, String name, Instant createdAt, Instant updatedAt

) {
}
