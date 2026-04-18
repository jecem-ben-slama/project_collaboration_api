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
        // Fetch existing note (this object still has its User and Project)
        Note note = noteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Update only the content
        note.setContent(newContent);

        // Save and map back
        // The 'note' object still has the 'user' field populated,
        // so ModelMapper will fill userId/userEmail in the DTO.
        Note updatedNote = noteRepo.save(note);
        return modelMapper.map(updatedNote, NoteDTO.class);
    }

    public void delete(Long id) {
        noteRepo.deleteById(id);
    }
}