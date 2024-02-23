package com.teletronics.notes.controller;

import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.request.UpdateNoteRequest;
import com.teletronics.notes.dto.response.NoteResponse;
import com.teletronics.notes.dto.response.NoteTextResponse;
import com.teletronics.notes.dto.response.PaginatedNoteResponse;
import com.teletronics.notes.enums.Tags;
import com.teletronics.notes.service.NotesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class NotesControllerTest {

    @InjectMocks
    private NotesController notesController;
    @Mock
    private NotesService notesService;

    @Test
    void createNote() {
         MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(notesService.createNote(any()))
                .thenReturn(new NoteResponse("note-1", "Test title", LocalDateTime.now()));
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) notesController.createNote(new CreateNoteRequest(
                "Test title", "test text", new HashSet<>()
        ));
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        NoteResponse response = (NoteResponse) responseEntity.getBody();
        assert response != null;
        assertThat(response.getId()).isEqualTo("note-1");

    }

//    @Test
//    void getAllNotes() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//
//        NoteResponse noteResponse = new NoteResponse("note-1", "title", LocalDateTime.now());
//        NoteResponse noteResponse2 = new NoteResponse("note-2", "title2", LocalDateTime.now());
//        PaginatedNoteResponse<NoteResponse> responseList = new PaginatedNoteResponse<>(Arrays.asList(noteResponse, noteResponse2)
//        , 2, 1,1);
//
//        String tags = "personal";
//         List<Tags> tagsSet = (tags!= null && !tags.isEmpty()) ? Arrays.stream(tags.split(",")).map(String::toUpperCase)
//                .map(Tags::valueOf)
//                .collect(Collectors.toList()) : null;
//
//        when(notesService.getAllNotesSortedAndFiltered(Optional.of(tagsSet), 1, 10)).thenReturn(responseList);
//
//        ResponseEntity<PaginatedNoteResponse<NoteResponse>> responseEntity = notesController.getAllNotes("personal",1,1);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
//        PaginatedNoteResponse<NoteResponse> response = (PaginatedNoteResponse) responseEntity.getBody();
//        assert response != null;
//        assertThat(response.getTotalNotes()).isEqualTo(2);
//    }

    @Test
    void getText() {
        when(notesService.getText(any())).thenReturn(new NoteTextResponse("Test note"));
        ResponseEntity<NoteTextResponse> responseEntity = notesController.getText("note-1");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        NoteTextResponse response = (NoteTextResponse) responseEntity.getBody();
        assert response != null;
        assertThat(response.getText()).isEqualTo("Test note");
    }

    @Test
    void updateNote() {
        when(notesService.updateNote(any(), any())).thenReturn(new NoteResponse("note-1", "Test title", LocalDateTime.now()));
        ResponseEntity<NoteResponse> responseEntity = notesController.updateNote("note-1",
                new UpdateNoteRequest("Test title", null, null));
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        NoteResponse response = (NoteResponse) responseEntity.getBody();
        assert response != null;
        assertThat(response.getTitle()).isEqualTo("Test title");
    }

    @Test
    void delete() {
    }

    @Test
    void getStats() {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("this", 2);
        countMap.put("is", 1);
        countMap.put("test", 1);
        when(notesService.getStats(any())).thenReturn(countMap);

        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) notesController.getStats("note-1");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Map<String, Integer> response = (Map<String, Integer>) responseEntity.getBody();
        assert response != null;
        assertEquals(countMap, response);
    }
}