package com.Soham.Workspace_Service.DTOs.Member;


import com.Soham.Common_Lib.Enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
      @Email @NotBlank String username,
      @NotNull ProjectRole role
) {
}
