package com.Soham.Workspace_Service.Servcies.Impl;

import com.Soham.Common_Lib.DTOs.UserDto;
import com.Soham.Common_Lib.Error.ResourceNotFoundException;
import com.Soham.Workspace_Service.Client.AccountClient;
import com.Soham.Workspace_Service.DTOs.Member.InviteMemberRequest;
import com.Soham.Workspace_Service.DTOs.Member.MemberResponse;
import com.Soham.Workspace_Service.DTOs.Member.UpdateMemberRequest;
import com.Soham.Workspace_Service.Entities.Project;
import com.Soham.Workspace_Service.Entities.ProjectMember;
import com.Soham.Workspace_Service.Entities.ProjectMemberId;
import com.Soham.Workspace_Service.Mapper.ProjectMemberRepsonseMapper;
import com.Soham.Workspace_Service.Repositories.ProjectMemberRepository;
import com.Soham.Workspace_Service.Repositories.ProjectRepository;
import com.Soham.Workspace_Service.Servcies.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ResourceClosedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImple implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepsonseMapper projectMemberRepsonseMapper;
    private final AccountClient accountClient;


    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId);
      return
        projectMemberRepository.findByIdProjectId(projectId).stream().map(projectMemberRepsonseMapper::toProjectMemberResponseFromMember).toList();



    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId);
        UserDto invitee= accountClient.getUserByEmail(request.username()).orElseThrow(
                ()-> new ResourceNotFoundException("User",request.username())
        );
        if(invitee.id().equals(userId)){
            throw new RuntimeException("Cannot invite userslef");
        }
        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,invitee.id());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Cannot be invited");
        }
        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(member);

        return projectMemberRepsonseMapper.toProjectMemberResponseFromMember(member);
    }



    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRequest request, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId);

        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember= projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberRepsonseMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public Void removeProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId);

        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Member not found");
        }
        projectMemberRepository.deleteById(projectMemberId);

        return null;
    }

    public Project getAccessibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project", projectId.toString()));
    }
}
