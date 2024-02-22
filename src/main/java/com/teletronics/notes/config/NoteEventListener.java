//package com.teletronics.notes.config;
//
//import com.teletronics.notes.document.Note;
//import com.teletronics.notes.service.NotesServiceAsync;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class NoteEventListener extends AbstractMongoEventListener<Note> {
//
//    private final NotesServiceAsync notesServiceAsync;
//
//    @Autowired
//    public NoteEventListener(NotesServiceAsync notesServiceAsync) {
//        this.notesServiceAsync = notesServiceAsync;
//    }
//
//    @Override
//    public void onBeforeSave(BeforeSaveEvent<Note> event){
//
//    }
//
//    @SneakyThrows
//    @Override
//    public void onAfterSave(AfterSaveEvent<Note> event) {
//        super.onAfterSave(event);
//        Note note = event.getSource();
//        if (note != null) {
//            notesServiceAsync.updateWordCounts(note.getId());
//        }
//    }
//}
