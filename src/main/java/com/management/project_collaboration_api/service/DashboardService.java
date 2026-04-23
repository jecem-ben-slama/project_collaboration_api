package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.*;
import com.management.project_collaboration_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private NoteRepository noteRepo;
    @Autowired
    private AffectationRepository affectationRepo;
    @Autowired
    private DashboardRepository dashboardRepo;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getGlobalStats() {
        long projectsCount = projectRepo.count();
        long usersCount = userRepo.count();
        long assignmentsCount = affectationRepo.count();

        double avg = projectsCount > 0 ? (double) assignmentsCount / projectsCount : 0;

        // Trends (LinkedHashMap to maintain chronological order)
        Map<String, Long> projectTrends = new LinkedHashMap<>();
        dashboardRepo.findProjectTrends().forEach(o -> projectTrends.put(o[0].toString(), ((Number) o[1]).longValue()));

        Map<String, Long> noteTrends = new LinkedHashMap<>();
        dashboardRepo.findNoteTrendsPerDay().forEach(o -> noteTrends.put(o[0].toString(), ((Number) o[1]).longValue()));

        // Notes per User
        Map<String, Long> notesPerUser = new LinkedHashMap<>();
        dashboardRepo.findNotesCountPerUser()
                .forEach(o -> notesPerUser.put(o[0].toString(), ((Number) o[1]).longValue()));

        // Status Map
        Map<String, Long> statusMap = dashboardRepo.countProjectsByStatus().stream()
                .collect(Collectors.toMap(o -> o[0].toString(), o -> ((Number) o[1]).longValue()));

        // Top Contributors
        List<UserWorkloadDTO> topContributors = dashboardRepo.findUserWorkload(PageRequest.of(0, 3)).stream()
                .map(o -> new UserWorkloadDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());

        return DashboardStatsDTO.builder()
                .totalProjects(projectsCount)
                .totalEmployees(usersCount)
                .totalNotes(noteRepo.count())
                .avgTeamSize(Math.round(avg * 100.0) / 100.0)
                .projectsByStatus(statusMap)
                .topContributors(topContributors)
                .projectTrends(projectTrends)
                .noteTrends(noteTrends)
                .notesPerUser(notesPerUser)
                .build();
    }
}