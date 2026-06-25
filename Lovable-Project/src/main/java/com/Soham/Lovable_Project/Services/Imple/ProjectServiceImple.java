package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Project.ProjectRequest;
import com.Soham.Lovable_Project.DTOs.Project.ProjectResponse;
import com.Soham.Lovable_Project.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Lovable_Project.Entities.Project;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.ProjectMemberId;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Enums.ProjectRole;
import com.Soham.Lovable_Project.Error.ResourceNotFoundException;
import com.Soham.Lovable_Project.Mapper.ProjectMapper;
import com.Soham.Lovable_Project.Repositories.ProjectMemberRepository;
import com.Soham.Lovable_Project.Repositories.ProjectRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Services.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImple implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;



    @Override
    public List<ProjectSummaryResponse> getAllProjects(Long userId) {
        return List.of();
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
       List<Project>   projects=projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryRepsonse((projects));
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {

        Project project=getAccesibleProjectById( id, userId);
        return projectMapper.toProjectResponse(project);



    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        // Extract the string explicitly to prevent processor binding mismatch
        String projectName = request.name();

        Project project = Project.builder()
                .name(projectName)
                .isPublic(false)
                .build();

        // Ensure this line precedes relational key mappings
        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .user(owner)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .build();

        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }


    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest req, Long userId) {


        Project project=getAccesibleProjectById( id, userId);;
        project.setName(req.name());
        project=projectRepository.save(project);
        return projectMapper.toProjectResponse(project);

    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project=getAccesibleProjectById( id, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);





    }

    public Project getAccesibleProjectById(Long projectId,Long userId){
        return projectRepository.findAccessibleProjectById(projectId,userId).orElseThrow(()-> new ResourceNotFoundException("Project",projectId.toString()));

    }
}
