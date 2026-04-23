package com.management.project_collaboration_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWorkloadDTO {
    private String userName;
    private long assignmentCount;
}