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

    public NoteDTO add(Note note) {
        return modelMapper.map(noteRepo.save(note), NoteDTO.class);
    }

    public List<NoteDTO> getByProject(Long projectId) {
        return noteRepo.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(n -> modelMapper.map(n, NoteDTO.class)).toList();
    }

    public NoteDTO update(Long id, String newContent) {
        Note note = noteRepo.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(newContent);
        return modelMapper.map(noteRepo.save(note), NoteDTO.class);
    }

    public void delete(Long id) {
        noteRepo.deleteById(id);
    }
}