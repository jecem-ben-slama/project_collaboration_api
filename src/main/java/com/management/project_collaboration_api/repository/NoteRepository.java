package com.management.project_collaboration_api.repository;

import com.management.project_collaboration_api.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Find all notes for a specific project to show the "chat" history
    List<Note> findByProjectIdOrderByCreatedAtDesc(Long projectId);
}