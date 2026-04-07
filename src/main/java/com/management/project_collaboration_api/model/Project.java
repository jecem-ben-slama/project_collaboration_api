package com.management.project_collaboration_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNED;

    public enum ProjectStatus {
        PLANNED, IN_PROGRESS, COMPLETED, ON_HOLD
    }
}