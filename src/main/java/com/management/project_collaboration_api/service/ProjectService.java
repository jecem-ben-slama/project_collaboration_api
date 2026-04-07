package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.ProjectDTO;
import com.management.project_collaboration_api.model.Project;
import com.management.project_collaboration_api.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ModelMapper modelMapper;

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
        Project p = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStatus(dto.getStatus());
        return modelMapper.map(projectRepo.save(p), ProjectDTO.class);
    }

    public void delete(Long id) {
        projectRepo.deleteById(id);
    }
}