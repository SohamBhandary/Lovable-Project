package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Project.ProjectRequest;
import com.Soham.Lovable_Project.DTOs.Project.ProjectResponse;
import com.Soham.Lovable_Project.DTOs.Project.ProjectSummaryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
     List<ProjectSummaryResponse> getAllProjects(Long userId);

     List<ProjectSummaryResponse> getUserProjects(Long userId);

     ProjectResponse getUserProjectById(Long id, Long userId);

     ProjectResponse createProject(ProjectRequest request, Long userId);

     ProjectResponse updateProject(Long id, ProjectRequest req, Long userId);

    void softDelete(Long id, Long userId);
}
