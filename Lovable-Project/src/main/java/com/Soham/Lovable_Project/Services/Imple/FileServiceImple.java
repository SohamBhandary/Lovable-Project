package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Project.FileContentResponse;
import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.Services.FIleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImple implements FIleService {
    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }
}
