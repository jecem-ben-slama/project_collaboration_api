package com.management.project_collaboration_api.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Generates Getters, Setters, equals, hashCode, and toString
@NoArgsConstructor // Generates the empty constructor
@AllArgsConstructor // Generates the constructor with all fields
public class AffectationRequestDTO {
    private Long userId;
    private Long projectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isTeamLeader;
}