package com.teletronics.notes.controller;

import com.teletronics.notes.document.Note;
import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.request.UpdateNoteRequest;
import com.teletronics.notes.dto.response.NoteResponse;
import com.teletronics.notes.dto.response.NoteTextResponse;
import com.teletronics.notes.dto.response.PaginatedNoteResponse;
import com.teletronics.notes.enums.Tags;
import com.teletronics.notes.error.FieldErrors;
import com.teletronics.notes.error.GenericError;
import com.teletronics.notes.error.ValidationError;
import com.teletronics.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("notes/v1")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService){
        this.notesService = notesService;
    }

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody CreateNoteRequest createNoteRequest){
        return new ResponseEntity<>(notesService.createNote(createNoteRequest), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<PaginatedNoteResponse<NoteResponse>> getAllNotes(
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Tags> tagsSet = (tags!= null && !tags.isEmpty()) ? Arrays.stream(tags.split(",")).map(String::toUpperCase)
                .map(Tags::valueOf)
                .collect(Collectors.toList()) : null;
        return new ResponseEntity<>(notesService.getAllNotesSortedAndFiltered(Optional.ofNullable(tagsSet), page, size), HttpStatus.OK);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteTextResponse> getText(@PathVariable String noteId){
        return new ResponseEntity<>(notesService.getText(noteId), HttpStatus.OK);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable String noteId,
        @RequestBody UpdateNoteRequest updateNoteRequest){
        return new ResponseEntity<>(notesService.updateNote(noteId, updateNoteRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> delete(@PathVariable String noteId){
        notesService.delete(noteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{noteId}/stats")
    public ResponseEntity<?> getStats(@PathVariable String noteId){

        Map<String, Integer> stats = notesService.getStats(noteId);
        if(stats == null || stats.isEmpty())
            return new ResponseEntity<>(Collections.singletonMap("message", "Stats not available for this note: " + noteId), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

}
