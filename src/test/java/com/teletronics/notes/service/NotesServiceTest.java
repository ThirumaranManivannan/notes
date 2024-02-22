package com.teletronics.notes.service;

import com.teletronics.notes.document.Note;
import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.request.UpdateNoteRequest;
import com.teletronics.notes.dto.response.NoteResponse;
import com.teletronics.notes.dto.response.NoteTextResponse;
import com.teletronics.notes.enums.Tags;
import com.teletronics.notes.exceptions.RepoException;
import com.teletronics.notes.repository.NotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class NotesServiceTest {

    @Mock
    NotesRepository notesRepository;
    @Mock
    NotesServiceAsync notesServiceAsync;
    @InjectMocks
    NotesService notesService;
    CreateNoteRequest createNoteRequest;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

    }

    @Test
    void createNote() {
        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        createNoteRequest = new CreateNoteRequest(
                "Maths", "This is maths, maths is fun", tags
        );
        Note note = new Note(createNoteRequest);
        note.setId("note-1");
        when(notesRepository.save(any())).thenReturn(note);
        //doNothing().when(notesServiceAsync).updateWordCounts(any());
        NoteResponse response = notesService.createNote(createNoteRequest);
        assertThat(response).isNotNull()
                .extracting(NoteResponse::getTitle, NoteResponse::getCreatedAt)
                .containsExactly(note.getTitle(), note.getCreationTime());

    }

    @Test
    void createNoteException() {
        UpdateNoteRequest updateNoteRequest = new UpdateNoteRequest(
                "Maths", "This is maths, maths is fun, added more notes", null
        );
        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        createNoteRequest = new CreateNoteRequest(
                "Maths", "This is maths, maths is fun", tags
        );
        Note note = new Note(createNoteRequest);
        note.setId("note-1");
        when(notesRepository.save(any())).thenThrow(new RepoException("simple throwing"));
        //doNothing().when(notesServiceAsync).updateWordCounts(any());
        //NoteResponse response = notesService.createNote(createNoteRequest);
        RepoException exception = Assertions.assertThrows(
                RepoException.class, ()->notesService.createNote(createNoteRequest)
        );

        assertEquals("Something went wrong while saving note.", exception.getMessage());
    }

    @Test
    void updateNote() {
        UpdateNoteRequest updateNoteRequest = new UpdateNoteRequest(
                "Maths", "This is maths, maths is fun, added more notes", null
        );
        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        createNoteRequest = new CreateNoteRequest(
                "Maths", "This is maths, maths is fun", tags
        );
        Note note = new Note(createNoteRequest);
        note.setId("note-1");

        note.setTitle(updateNoteRequest.getTitle());
        note.setText(updateNoteRequest.getText());
        when(notesRepository.save(any())).thenReturn(note);
        when(notesRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(note));
        //doNothing().when(notesServiceAsync).updateWordCounts(any());
        NoteResponse response = notesService.updateNote(note.getId(), updateNoteRequest);
        assertThat(response).isNotNull()
                .extracting(NoteResponse::getTitle, NoteResponse::getCreatedAt)
                .containsExactly(note.getTitle(), note.getCreationTime());
    }

    @Test
    void getAllNotesSortedAndFiltered() {
    }

    @Test
    void getText() {
        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        createNoteRequest = new CreateNoteRequest(
                "Maths", "This is maths, maths is fun", tags
        );
        Note note = new Note(createNoteRequest);
        note.setId("note-1");

        when(notesRepository.findById(any())).thenReturn(java.util.Optional.of(note));
        NoteTextResponse response = notesService.getText(note.getId());
        assertThat(response).isNotNull();
        assertThat(response.getText()).isEqualTo(note.getText());

    }


    @Test
    void getStats() {
        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        createNoteRequest = new CreateNoteRequest(
                "Maths", "This is maths, maths is fun", tags
        );
        Note note = new Note(createNoteRequest);
        note.setId("note-1");

        Map<String, Integer> wordCountMap = new HashMap<>();
        wordCountMap.put("is", 2);
        wordCountMap.put("maths", 2);
        wordCountMap.put("This", 1);
        wordCountMap.put("fun", 1);
        note.setWordCountMap(wordCountMap);

        when(notesRepository.findById(any())).thenReturn(java.util.Optional.of(note));
        Map<String, Integer> wordStatsMap = notesService.getStats(note.getId());
        assertThat(wordStatsMap).isNotNull();
        assertThat(wordStatsMap.keySet()).isEqualTo(note.getWordCountMap().keySet());
        assertThat(wordStatsMap.values()).isEqualTo(note.getWordCountMap().values());
    }
}