package com.Soham.Workspace_Service.Servcies;


import com.Soham.Workspace_Service.DTOs.Project.FileContentResponse;
import com.Soham.Workspace_Service.DTOs.Project.FileTreeResponse;

public interface ProjectFIleService {
    FileTreeResponse getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);

}

