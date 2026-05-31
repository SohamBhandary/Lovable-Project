package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Project.FileContentResponse;
import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.DTOs.Project.FileTreeResponse;
import com.Soham.Lovable_Project.Services.FIleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {
    private final FIleService fIleService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId){
        Long userId=1L;
        return ResponseEntity.ok(fIleService.getFileTree(projectId,userId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse>  getFile(
            @PathVariable Long projectId,
            @PathVariable String path
    ){
        Long userId=1L;
        return ResponseEntity.ok(fIleService.getFileContent(projectId,path,userId));
    }
}
