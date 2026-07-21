package com.Soham.Workspace_Service.DTOs.Member;



import com.Soham.Common_Lib.Enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole role,
        Instant invitedAt
) {
}
