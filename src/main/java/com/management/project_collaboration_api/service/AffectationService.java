package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.AffectationRequestDTO;
import com.management.project_collaboration_api.dto.ProjectDetailDTO;
import com.management.project_collaboration_api.dto.UserDTO;
import com.management.project_collaboration_api.model.*;
import com.management.project_collaboration_api.repository.*;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AffectationService {
    @Autowired
    private AffectationRepository affectationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private ModelMapper modelMapper;

    public void assign(AffectationRequestDTO dto) {
        // Check if the user is already assigned to this project
        if (affectationRepo.existsByUserIdAndProjectId(dto.getUserId(), dto.getProjectId())) {
            // The Handler looks for "already assigned" to trigger a 409 Conflict
            throw new RuntimeException("This user is already assigned to this project.");
        }

        User u = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project p = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Affectation aff = new Affectation();
        aff.setStartDate(dto.getStartDate());
        aff.setEndDate(dto.getEndDate());
        aff.setTeamLeader(dto.isTeamLeader());
        aff.setUser(u);
        aff.setProject(p);

        affectationRepo.save(aff);
    }
    public List<Affectation> getByProject(Long projectId) {
        return affectationRepo.findByProjectId(projectId);
    }
    
@Transactional(readOnly = true)
public List<ProjectDetailDTO> getAllProjectsForAdmin() {
    List<Project> projects = projectRepo.findAll();

    return projects.stream().map(project -> {
        // 1. Basic mapping (ID, Title, Description, etc.)
        ProjectDetailDTO dto = modelMapper.map(project, ProjectDetailDTO.class);
        
        // 2. Safely map the team (will return an empty list if no users assigned)
        if (project.getAffectations() != null) {
            List<UserDTO> team = project.getAffectations().stream()
                    .map(aff -> modelMapper.map(aff.getUser(), UserDTO.class))
                    .toList();
            dto.setAssignedUsers(team);

            // 3. Find the team leader only if the list isn't empty
            project.getAffectations().stream()
                    .filter(Affectation::isTeamLeader)
                    .findFirst()
                    .ifPresentOrElse(
                        leaderAff -> {
                            dto.setLeaderId(leaderAff.getUser().getId());
                            dto.setLeaderName(leaderAff.getUser().getName());
                        },
                        () -> {
                            // Explicitly set to null or "No Leader" if none found
                            dto.setLeaderId(null);
                            dto.setLeaderName("No Leader Assigned");
                        }
                    );
        } else {
            dto.setAssignedUsers(new ArrayList<>());
            dto.setLeaderName("No Team");
        }

        return dto;
    }).toList();
}

    public void remove(Long id) {
        affectationRepo.deleteById(id);
    }
}