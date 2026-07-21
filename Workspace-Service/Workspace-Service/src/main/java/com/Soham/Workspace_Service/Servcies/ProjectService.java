package com.Soham.Workspace_Service.Servcies;



import com.Soham.Workspace_Service.DTOs.Project.ProjectRequest;
import com.Soham.Workspace_Service.DTOs.Project.ProjectResponse;
import com.Soham.Workspace_Service.DTOs.Project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
     List<ProjectSummaryResponse> getAllProjects();

     List<ProjectSummaryResponse> getUserProjects();

     ProjectSummaryResponse getUserProjectById(Long id);

     ProjectResponse createProject(ProjectRequest request);

     ProjectResponse updateProject(Long id, ProjectRequest req);

    void softDelete(Long id);
}
