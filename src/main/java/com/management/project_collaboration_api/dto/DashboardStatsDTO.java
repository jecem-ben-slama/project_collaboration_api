package com.management.project_collaboration_api.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalProjects;
    private long totalEmployees;
    private long totalNotes;
    private double avgTeamSize;
    private Map<String, Long> projectsByStatus;
    private List<UserWorkloadDTO> topContributors;

    // Graphs
    private Map<String, Long> projectTrends; // Projects per month
    private Map<String, Long> noteTrends; // Notes per day
    private Map<String, Long> notesPerUser; // User vs Note count
}