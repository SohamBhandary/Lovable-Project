package com.Soham.Workspace_Service.DTOs.Member;


import com.Soham.Common_Lib.Enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRequest( @NotNull ProjectRole role) {
}
