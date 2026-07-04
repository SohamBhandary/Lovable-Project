package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Project.FileContentResponse;
import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.Entities.ProjectFile;

import java.util.List;
import java.util.Optional;

public interface ProjectFIleService {
     List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);

    void saveFile(Long projectId, String filePath, String fileContent);

}

