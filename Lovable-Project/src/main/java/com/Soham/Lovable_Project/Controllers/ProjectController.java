package com.Soham.Lovable_Project.Controllers;

import com.Soham.Lovable_Project.DTOs.Project.ProjectRequest;
import com.Soham.Lovable_Project.DTOs.Project.ProjectResponse;
import com.Soham.Lovable_Project.DTOs.Project.ProjectSummaryResponse;
import com.Soham.Lovable_Project.Deploy.DeployResponse;
import com.Soham.Lovable_Project.Security.AuthUtil;
import com.Soham.Lovable_Project.Services.DeploymentService;
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
    private final AuthUtil authUtil;
    private final DeploymentService deploymentService;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){

        return ResponseEntity.ok(projectService.getUserProjects());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSummaryResponse>getProjectById(@PathVariable Long id){

        return ResponseEntity.ok(projectService.getUserProjectById(id));

    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id ,@RequestBody @Valid ProjectRequest req){

        return ResponseEntity.ok(projectService.updateProject(id,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){

        projectService.softDelete(id);
        return  ResponseEntity.noContent().build();






    }

    @PostMapping("/{id}/deploy")
    public ResponseEntity<DeployResponse> deployProject(@PathVariable Long id) {
        return ResponseEntity.ok(deploymentService.deploy(id));
    }








}
