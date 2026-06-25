package com.Soham.Lovable_Project.DTOs.Member;

import com.Soham.Lovable_Project.Enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
      @Email @NotBlank String username,
      @NotNull ProjectRole role
) {
}
