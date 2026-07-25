package com.Soham.Workspace_Service.Servcies;


import com.Soham.Workspace_Service.DTOs.Project.DeployResponse;

public interface DeploymentService {
    DeployResponse deploy(Long projectId);
}
