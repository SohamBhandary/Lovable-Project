package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Member.InviteMemberRequest;
import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.DTOs.Member.UpdateMemberRequest;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
     List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

   MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRequest request, Long userId);

   Void removeProjectMember(Long projectId, Long memberId, Long userId);
}
