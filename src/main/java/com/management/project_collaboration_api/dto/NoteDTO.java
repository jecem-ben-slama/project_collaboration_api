package com.management.project_collaboration_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    // These will be mapped automatically from the User object in Note
    private Long userId;
    private String userEmail;
    private String userName;

    // Getters and Setters...
}