package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Project.ProjectRequest;
import com.Soham.Lovable_Project.DTOs.Project.ProjectResponse;
import com.Soham.Lovable_Project.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Lovable_Project.Services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){
        Long userId=1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse>getProjectById(@PathVariable Long id){
        Long userId=1L;
        return ResponseEntity.ok(projectService.getUserProjectById(id,userId));

    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request){
        Long userId=1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request,userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id ,@RequestBody @Valid ProjectRequest req){
        Long userId=1L;
        return ResponseEntity.ok(projectService.updateProject(id,req,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Long userId=1L;
        projectService.softDelete(id,userId);
        return  ResponseEntity.noContent().build();






    }








}
