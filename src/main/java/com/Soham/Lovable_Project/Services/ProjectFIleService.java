package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Project.FileContentResponse;
import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.DTOs.Project.FileTreeResponse;
import com.Soham.Lovable_Project.Entities.ProjectFile;

import java.util.List;
import java.util.Optional;

public interface ProjectFIleService {
    FileTreeResponse getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);

}

