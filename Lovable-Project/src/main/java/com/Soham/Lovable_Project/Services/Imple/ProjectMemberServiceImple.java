package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Member.InviteMemberRequest;
import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.DTOs.Member.UpdateMemberRequest;
import com.Soham.Lovable_Project.Entities.Project;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.ProjectMemberId;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Error.ResourceNotFoundException;
import com.Soham.Lovable_Project.Mapper.ProjectMemberRepsonseMapper;
import com.Soham.Lovable_Project.Repositories.ProjectMemberRepository;
import com.Soham.Lovable_Project.Repositories.ProjectRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Services.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImple implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepsonseMapper projectMemberRepsonseMapper;
    private final UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);
      return
        projectMemberRepository.findByIdProjectId(projectId).stream().map(projectMemberRepsonseMapper::toProjectResponseFromMember).toList();



    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);
        User invitee=userRepository.findByUsername(request.username()).orElseThrow();
        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Cannot invite userslef");
        }
        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Cannot be invited");
        }
        ProjectMember member= ProjectMember.builder().id(projectMemberId).project(project).user(invitee).projectRole(request.role()).invitedAt(Instant.now()).build();
        projectMemberRepository.save(member);

        return projectMemberRepsonseMapper.toProjectResponseFromMember(member);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRequest request, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);

        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember= projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberRepsonseMapper.toProjectResponseFromMember(projectMember);
    }

    @Override
    public Void removeProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);

        ProjectMemberId projectMemberId= new ProjectMemberId(projectId,memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Member not found");
        }
        projectMemberRepository.deleteById(projectMemberId);

        return null;
    }


    public Project getAccesibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();

    }
}
