package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Member.InviteMemberRequest;
import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.DTOs.Member.UpdateMemberRequest;
import com.Soham.Lovable_Project.Entities.Project;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.ProjectMemberId;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Mapper.ProjectMemberRepsonseMapper;
import com.Soham.Lovable_Project.Repositories.ProjectMemberRepository;
import com.Soham.Lovable_Project.Repositories.ProjectRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Services.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImple implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepsonseMapper projectMemberRepsonseMapper;
    private final UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);
        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberRepsonseMapper.toProjectResponseFromOwner(project.getOwner()));
        memberResponseList.addAll(projectMemberRepository.findByIdProjectId(projectId).stream().map(projectMemberRepsonseMapper::toProjectResponseFromMember).toList());


        return memberResponseList;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project = getAccesibleProjectById(projectId, userId);
        if(!project.getOwner().getId().equals(userId))
        {
            throw new RuntimeException("accesdenid");
        }
        User invitee=userRepository.findByEmail(request.email()).orElseThrow();
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
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }


    public Project getAccesibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();

    }
}
