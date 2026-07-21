package com.Soham.Workspace_Service.Servcies;


import com.Soham.Workspace_Service.DTOs.Member.InviteMemberRequest;
import com.Soham.Workspace_Service.DTOs.Member.MemberResponse;
import com.Soham.Workspace_Service.DTOs.Member.UpdateMemberRequest;

import java.util.List;

public interface ProjectMemberService {
     List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

   MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRequest request, Long userId);

   Void removeProjectMember(Long projectId, Long memberId, Long userId);
}
