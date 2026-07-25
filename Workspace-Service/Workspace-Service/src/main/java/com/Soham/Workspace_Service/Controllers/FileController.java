package com.Soham.Workspace_Service.Controllers;


import com.Soham.Common_Lib.DTOs.FileTreeDto;
import com.Soham.Workspace_Service.DTOs.Project.FileContentResponse;
import com.Soham.Workspace_Service.Servcies.ProjectFIleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/files")
public class FileController {
    private final ProjectFIleService fIleService;

    @GetMapping
    public ResponseEntity<FileTreeDto> getFileTree(@PathVariable Long projectId) {
        return ResponseEntity.ok(fIleService.getFileTree(projectId));
    }

    @GetMapping("/content")
    public ResponseEntity<String> getFile(
            @PathVariable Long projectId,
            @RequestParam String path) {
        return ResponseEntity.ok(fIleService.getFileContent(projectId, path));
    }
}
