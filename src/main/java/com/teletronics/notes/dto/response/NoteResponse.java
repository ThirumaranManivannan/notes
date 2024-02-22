package com.teletronics.notes.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teletronics.notes.document.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@AllArgsConstructor
public class NoteResponse {
    private String id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public  NoteResponse(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.createdAt = note.getCreationTime();
    }
}
