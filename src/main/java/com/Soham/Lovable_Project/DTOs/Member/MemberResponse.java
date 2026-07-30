package com.Soham.Lovable_Project.DTOs.Member;

import com.Soham.Lovable_Project.Enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole role,
        Instant invitedAt
) {
}
