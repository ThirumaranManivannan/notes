package com.teletronics.notes.dto.response;

import com.teletronics.notes.document.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteTextResponse {
    private String text;

    public NoteTextResponse(Note note) {
        this.text = note.getText();
    }
}
