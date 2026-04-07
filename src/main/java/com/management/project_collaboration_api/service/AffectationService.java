package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.AffectationRequestDTO;
import com.management.project_collaboration_api.model.*;
import com.management.project_collaboration_api.repository.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AffectationService {
    @Autowired
    private AffectationRepository affectationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProjectRepository projectRepo;

    public void assign(AffectationRequestDTO dto) {
        User u = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
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

    public void remove(Long id) {
        affectationRepo.deleteById(id);
    }
}