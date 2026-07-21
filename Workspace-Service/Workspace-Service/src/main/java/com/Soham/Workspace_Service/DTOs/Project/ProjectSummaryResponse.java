package com.Soham.Workspace_Service.DTOs.Project;



import com.Soham.Common_Lib.Enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(Long id, String name, Instant createdAt, Instant updatedAt, ProjectRole role) {
}
