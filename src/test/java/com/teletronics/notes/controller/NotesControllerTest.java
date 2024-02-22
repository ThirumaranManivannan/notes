package com.teletronics.notes.controller;

import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.response.NoteResponse;
import com.teletronics.notes.service.NotesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NotesControllerTest {

    @MockBean
    private NotesService notesService;
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void createNote() {
        when(notesService.createNote(new CreateNoteRequest())).thenReturn(new NoteResponse("note-1", "maths", LocalDateTime.now()));
    }

    @Test
    void getAllNotes() {
    }

    @Test
    void getText() {
    }

    @Test
    void updateNote() {
    }

    @Test
    void delete() {
    }

    @Test
    void getStats() {
    }
}