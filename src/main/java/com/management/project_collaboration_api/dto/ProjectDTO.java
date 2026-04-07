package com.management.project_collaboration_api.dto;

import lombok.Data;
import com.management.project_collaboration_api.model.Project.ProjectStatus;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
}