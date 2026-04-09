package com.management.project_collaboration_api.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String categoryLabel; // We just send the name of the category
    private Long categoryId; // Added categoryId
}