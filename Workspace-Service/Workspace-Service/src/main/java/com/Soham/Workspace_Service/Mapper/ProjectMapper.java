package com.Soham.Workspace_Service.Mapper;

import com.Soham.Common_Lib.Enums.ProjectRole;
import com.Soham.Workspace_Service.DTOs.Project.ProjectResponse;
import com.Soham.Workspace_Service.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Workspace_Service.Entities.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);
    ProjectSummaryResponse toProjectSummaryResponse(Project project, ProjectRole role);
    List<ProjectSummaryResponse> toListOfProjectSummaryRepsonse(List<Project> projects);
}
