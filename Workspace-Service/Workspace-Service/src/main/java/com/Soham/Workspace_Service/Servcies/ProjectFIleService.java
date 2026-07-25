package com.Soham.Workspace_Service.Servcies;


import com.Soham.Common_Lib.DTOs.FileTreeDto;
import com.Soham.Workspace_Service.DTOs.Project.FileContentResponse;

public interface ProjectFIleService {
    FileTreeDto getFileTree(Long projectId);

    String getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);

}

