package com.management.project_collaboration_api.controller;

import com.management.project_collaboration_api.dto.AffectationRequestDTO;
import com.management.project_collaboration_api.dto.ProjectDTO;
import com.management.project_collaboration_api.dto.ProjectDetailDTO;
import com.management.project_collaboration_api.service.AffectationService;
import com.management.project_collaboration_api.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AffectationService affectationService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<ProjectDTO> getAll() {
        return projectService.getAll();
    }
    @GetMapping("/my-assigned")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public ResponseEntity<List<ProjectDTO>> getMyProjects(Principal principal) {
    // principal.getName() automatically gives you the email from the JWT
    return ResponseEntity.ok(projectService.getMyAssignedProjects(principal.getName()));
}

   @GetMapping("/user/{userId}")
@PreAuthorize("hasRole('ADMIN')") // Usually only Admins should search by specific IDs
public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(projectService.getMyProjects(userId));
}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ProjectDTO getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectDTO create(@RequestBody ProjectDTO dto) {
        return projectService.create(dto);
    }

   

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectDTO update(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        return projectService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assign(@RequestBody AffectationRequestDTO dto) {
        affectationService.assign(dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{projectId}/remove-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectService.removeAssignment(projectId, userId);
        return ResponseEntity.ok("User removed from project successfully");
    }

    @GetMapping("/all-details")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<ProjectDetailDTO>> getAllProjectDetails() {
    return ResponseEntity.ok(affectationService.getAllProjectsForAdmin());
}
}