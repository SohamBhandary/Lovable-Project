package com.Soham.Lovable_Project.Services;

import com.Soham.Lovable_Project.DTOs.Project.FileContentResponse;
import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface FIleService {
     List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
