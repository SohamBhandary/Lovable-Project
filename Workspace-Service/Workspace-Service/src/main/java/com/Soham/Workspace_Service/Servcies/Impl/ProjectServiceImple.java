package com.Soham.Workspace_Service.Servcies.Impl;


import com.Soham.Common_Lib.DTOs.PlanDto;
import com.Soham.Common_Lib.Enums.ProjectRole;
import com.Soham.Common_Lib.Error.BadRequestException;
import com.Soham.Common_Lib.Error.ResourceNotFoundException;
import com.Soham.Common_Lib.Security.AuthUtil;
import com.Soham.Workspace_Service.Client.AccountClient;
import com.Soham.Workspace_Service.DTOs.Project.ProjectRequest;
import com.Soham.Workspace_Service.DTOs.Project.ProjectResponse;
import com.Soham.Workspace_Service.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Workspace_Service.Entities.Project;
import com.Soham.Workspace_Service.Entities.ProjectMember;
import com.Soham.Workspace_Service.Entities.ProjectMemberId;
import com.Soham.Workspace_Service.Mapper.ProjectMapper;
import com.Soham.Workspace_Service.Repositories.ProjectMemberRepository;
import com.Soham.Workspace_Service.Repositories.ProjectRepository;
import com.Soham.Workspace_Service.Servcies.ProjectService;
import com.Soham.Workspace_Service.Servcies.ProjectTemplateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.owner;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImple implements ProjectService {
    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    private final ProjectTemplateService projectTemplateService;
    private final AccountClient accountClient;


    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        if(!canCreateProject()){

            throw  new BadRequestException("User cannot create new project in the current plan ,please upgrade to continue");

        }
        Long ownerUserId = authUtil.getCurrentUserId();
//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User", userId.toString())
//        );



        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();
        project = projectRepository.save(project);


        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), ownerUserId);
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .build();

        projectMemberRepository.save(projectMember);
        projectTemplateService.initializeProjectFromTemplate(project.getId());

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getAllProjects() {
        return List.of();
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId = authUtil.getCurrentUserId();
        var projectsWithRoles = projectRepository.findAllAccessibleByUser(userId);
        return projectsWithRoles.stream()
                .map(p -> projectMapper.toProjectSummaryResponse(p.getProject(), p.getRole()))
                .toList();
    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectSummaryResponse getUserProjectById(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        var projectWithRole = projectRepository.findAccessibleProjectByIdWithRole(projectId, userId)
                .orElseThrow(() -> new BadRequestException("Project Not Found"));

        return projectMapper.toProjectSummaryResponse(projectWithRole.getProject(), projectWithRole.getRole());
    }

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);

        project.setName(request.name());
        project = projectRepository.save(project);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#projectId)")
    public void softDelete(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    ///  INTERNAL FUNCTIONS

    public Project getAccessibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectByIdWithRole(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString())).getProject();
    }


    private boolean canCreateProject() {
        Long userId = authUtil.getCurrentUserId();
        if (userId == null) {
            return false;
        }
        PlanDto plan = accountClient.getCurrentSubscribedPlanByUser();

        int maxAllowed = plan.maxProjects();
        int ownedCount = projectMemberRepository.countProjectOwnedByUser(userId);

        return ownedCount < maxAllowed;
    }

}
