package com.management.project_collaboration_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private String content;
    private LocalDateTime createdAt;
    private String authorName;
    private Long projectId;
}