package com.management.project_collaboration_api.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDetailDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private String status;

    // Team Leader details (Extracted from the leader affectation)
    private Long leaderId;
    private String leaderName;

    // The whole team
    private List<UserDTO> assignedUsers;
}