package com.management.project_collaboration_api.controller;

import com.management.project_collaboration_api.dto.AffectationRequestDTO;
import com.management.project_collaboration_api.dto.ProjectDTO;
import com.management.project_collaboration_api.service.AffectationService;
import com.management.project_collaboration_api.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    @PreAuthorize("hasRole('ADMIN')")
    public String assign(@RequestBody AffectationRequestDTO request) {
        affectationService.assign(request);
        return "Member assigned successfully";
    }
}