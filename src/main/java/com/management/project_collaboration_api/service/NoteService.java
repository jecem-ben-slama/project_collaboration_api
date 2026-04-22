package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.NoteDTO;
import com.management.project_collaboration_api.model.*;
import com.management.project_collaboration_api.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepo;

    public NoteDTO add(Note note, String email) {
        // 1. Find the logged-in user
        User author = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Set the author to the note
        note.setUser(author);

        // 3. Save and return as DTO
        Note savedNote = noteRepo.save(note);
        return modelMapper.map(savedNote, NoteDTO.class);
    }

    public List<NoteDTO> getByProject(Long projectId) {
        return noteRepo.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(n -> modelMapper.map(n, NoteDTO.class))
                .toList();
    }


    public NoteDTO update(Long id, String newContent) {
        Note note = noteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Strip quotes if they exist
        if (newContent != null && newContent.startsWith("\"") && newContent.endsWith("\"")) {
            newContent = newContent.substring(1, newContent.length() - 1);
        }

        // 1. Update the content
        note.setContent(newContent);

        // 2. Update the timestamp to the current time
        // If your entity uses @UpdateTimestamp, this happens automatically,
        // otherwise, do it manually:
        note.setCreatedAt(java.time.LocalDateTime.now());

        Note updatedNote = noteRepo.save(note);
        return modelMapper.map(updatedNote, NoteDTO.class);
    }

    public void delete(Long id) {
        noteRepo.deleteById(id);
    }
}