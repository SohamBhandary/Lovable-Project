package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Project.ProjectRequest;
import com.Soham.Lovable_Project.DTOs.Project.ProjectResponse;
import com.Soham.Lovable_Project.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Lovable_Project.Entities.Project;
import com.Soham.Lovable_Project.Entities.User;
import com.Soham.Lovable_Project.Mapper.ProjectMapper;
import com.Soham.Lovable_Project.Repositories.ProjectRepository;
import com.Soham.Lovable_Project.Repositories.UserRepository;
import com.Soham.Lovable_Project.Services.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImple implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;


    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow();
        Project project= Project.builder().name(request.name()).isPublic(false).owner(owner).build();
        project=projectRepository.save(project);
      return   projectMapper.toProjectResponse(project);



    }

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
        return null;
    }


    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest req, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
