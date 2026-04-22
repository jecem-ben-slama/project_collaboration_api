package com.management.project_collaboration_api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MyProjectDTO {
    private ProjectDTO project; // The existing project details
    private boolean isTeamLeader;
    private LocalDate startDate;
    private LocalDate endDate;

}