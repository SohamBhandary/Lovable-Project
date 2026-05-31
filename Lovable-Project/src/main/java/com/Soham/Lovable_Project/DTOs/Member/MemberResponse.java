package com.Soham.Lovable_Project.DTOs.Member;

import com.Soham.Lovable_Project.Enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(Long id, String email, String name, String avatarURL,
                             ProjectRole role,
                             Instant invitedAt) {
}
