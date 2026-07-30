package com.Soham.Lovable_Project.DTOs.Member;

import com.Soham.Lovable_Project.Enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRequest( @NotNull ProjectRole role) {
}
