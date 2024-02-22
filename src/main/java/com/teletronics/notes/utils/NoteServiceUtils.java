package com.teletronics.notes.utils;

import com.teletronics.notes.document.Note;
import com.teletronics.notes.dto.request.UpdateNoteRequest;

import java.time.LocalDateTime;

public class NoteServiceUtils {

    public static boolean createNoteFromUpdateRequest(Note note, UpdateNoteRequest updateNoteRequest){
        boolean runStats = false;
        if(updateNoteRequest.getTitle() != null && !updateNoteRequest.getTitle().isEmpty())
            note.setTitle(updateNoteRequest.getTitle());
        if(updateNoteRequest.getText() != null && !updateNoteRequest.getText().isEmpty()) {
            runStats = !updateNoteRequest.getText().equals(note.getText());
            note.setText(updateNoteRequest.getText());
        }
        if(updateNoteRequest.getTags() != null && !updateNoteRequest.getTags().isEmpty())
            note.setTags(updateNoteRequest.getTags());
        note.setUpdatedTime(LocalDateTime.now());
        return runStats;
    }
}
