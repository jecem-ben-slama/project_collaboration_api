package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.ProjectDTO;
import com.management.project_collaboration_api.model.Affectation;
import com.management.project_collaboration_api.model.Project;
import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.repository.AffectationRepository;
import com.management.project_collaboration_api.repository.ProjectRepository;
import com.management.project_collaboration_api.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ModelMapper modelMapper;
@Autowired
private AffectationRepository affectationRepo;
@Autowired
private UserRepository userRepo;


public List<ProjectDTO> getMyAssignedProjects(String email) {
    // 1. Find the user based on the email from JWT
    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 2. Get assignments using the User's ID
    List<Affectation> assignments = affectationRepo.findByUserId(user.getId());

    // 3. Map the projects to DTOs
    return assignments.stream()
            .map(aff -> modelMapper.map(aff.getProject(), ProjectDTO.class))
            .collect(Collectors.toList());
}
public List<ProjectDTO> getMyProjects(long userId) {
    // 1. Get all assignments for this user
    List<Affectation> assignments = affectationRepo.findByUserId(userId);

    // 2. Extract projects from assignments and map to DTO
    return assignments.stream()
            .map(affectation -> modelMapper.map(affectation.getProject(), ProjectDTO.class))
            .collect(Collectors.toList());
}
    public List<ProjectDTO> getAll() {
        return projectRepo.findAll().stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class)).toList();
    }

    public ProjectDTO getById(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        return modelMapper.map(project, ProjectDTO.class);
    }
   
    public ProjectDTO create(ProjectDTO dto) {
        Project p = modelMapper.map(dto, Project.class);
        return modelMapper.map(projectRepo.save(p), ProjectDTO.class);
    }

    public ProjectDTO update(Long id, ProjectDTO dto) {
        // 1. Find the existing project
        Project existingProject = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // 2. Configure ModelMapper to skip null fields
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        // 3. Map DTO onto the EXISTING entity (this only overwrites specified fields)
        modelMapper.map(dto, existingProject);

        // 4. Save and return
        return modelMapper.map(projectRepo.save(existingProject), ProjectDTO.class);
    }
    
    public void removeAssignment(Long projectId, Long userId) {
        Affectation affectation = affectationRepo.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Assignment not found for this user and project"));

        affectationRepo.delete(affectation);
    }
    public void delete(Long id) {
        projectRepo.deleteById(id);
    }
}