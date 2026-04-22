package com.management.project_collaboration_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Add this
@AllArgsConstructor
@Data
public class NoteRequest {
    private String content;
}