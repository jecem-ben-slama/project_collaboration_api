package com.management.project_collaboration_api.controller;

import com.management.project_collaboration_api.dto.NoteDTO;
import com.management.project_collaboration_api.model.Note;
import com.management.project_collaboration_api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired private NoteService noteService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public NoteDTO add(@RequestBody Note note) {
        return noteService.add(note);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<NoteDTO> getByProject(@PathVariable Long projectId) {
        return noteService.getByProject(projectId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public NoteDTO update(@PathVariable Long id, @RequestBody String content) {
        return noteService.update(id, content);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        noteService.delete(id);
    }
}