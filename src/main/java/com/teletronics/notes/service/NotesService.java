package com.teletronics.notes.service;

import com.mongodb.MongoClientException;
import com.teletronics.notes.document.Note;
import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.request.UpdateNoteRequest;
import com.teletronics.notes.dto.response.NoteResponse;
import com.teletronics.notes.dto.response.NoteTextResponse;
import com.teletronics.notes.dto.response.PaginatedNoteResponse;
import com.teletronics.notes.enums.Tags;
import com.teletronics.notes.exceptions.NoteIdInvalidException;
import com.teletronics.notes.exceptions.NotesNotFoundException;
import com.teletronics.notes.exceptions.RepoException;
import com.teletronics.notes.repository.NotesRepository;
import com.teletronics.notes.utils.NoteServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotesService {

    private final NotesServiceAsync notesServiceAsync;
    private final NotesRepository notesRepository;
    @Autowired
    public NotesService(NotesServiceAsync notesServiceAsync, NotesRepository notesRepository){
        this.notesServiceAsync = notesServiceAsync;
        this.notesRepository = notesRepository;
    }

    public NoteResponse createNote(CreateNoteRequest createNoteRequest){
        Note newNote = new Note(createNoteRequest);
        try {
            Note note = notesRepository.save(newNote);
            notesServiceAsync.updateWordCounts(note.getId(), note.getText());
            return new NoteResponse(note);
        }catch (Exception exception){
            log.error("cannot save note, mongo error: " + exception.getMessage());
            throw new RepoException("Something went wrong while saving note.");
        }
    }


    public NoteResponse updateNote(String noteId, UpdateNoteRequest updateNoteRequest){
        Note note = getNote(noteId);
        if(note != null && note.getId() != null) {
            boolean runStats = NoteServiceUtils.createNoteFromUpdateRequest(note, updateNoteRequest);
            try {
                Note updatedNote = notesRepository.save(note);
                if(runStats)
                    notesServiceAsync.updateWordCounts(note.getId(), note.getText());
                return new NoteResponse(updatedNote);
            } catch (Exception exception) {
                log.error("cannot save note, mongo error: " + exception.getMessage());
                throw new RepoException("Something went wrong while saving note.");
            }
        }else {
            throw new NotesNotFoundException("Cannot find note for this id: " + noteId);
        }
    }


    public PaginatedNoteResponse<NoteResponse> getAllNotesSortedAndFiltered(Optional<List<Tags>> tags, int page, int size) {
        try {
            page = page - 1;
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationTime"));

            Page<Note> resultPage = tags.map(tag -> notesRepository.findByTagsIn(tag, pageRequest))
                    .orElseGet(() -> notesRepository.findAll(pageRequest));

            if(resultPage.getContent() != null && !resultPage.getContent().isEmpty()) {

                List<NoteResponse> notes = resultPage.getContent().stream()
                        .map(NoteResponse::new)
                        .collect(Collectors.toList());
                long totalItems = resultPage.getTotalElements();
                int totalPages = resultPage.getTotalPages();
                int currentPage = resultPage.getNumber() + 1;
                return new PaginatedNoteResponse<NoteResponse>(notes, totalItems, totalPages, currentPage);
            }else {
                throw new NotesNotFoundException("No notes available. Try to use valid filters");
            }
        }catch (MongoClientException exception){
            log.error("cannot retrieve notes, error: " + exception.getMessage());
            throw new RepoException("Something went wrong while saving note.");
        }
    }

    public NoteTextResponse getText(String noteId){
        if(noteId == null || noteId.isEmpty())
            throw new NoteIdInvalidException("Note Id must be filled");
        Note note = notesRepository.findById(noteId).orElseThrow(() -> new NotesNotFoundException("Cannot find note for this id: " + noteId));
        return new NoteTextResponse(note);
    }

    private Note getNote(String noteId){
        if(noteId == null || noteId.isEmpty())
            throw new NoteIdInvalidException("Note Id must be filled");
        return notesRepository.findById(noteId).orElseThrow(() -> new NotesNotFoundException("Cannot find note for this id: " + noteId));
    }

    public void delete(String noteId){
        Note note = getNote(noteId);
        try {
            notesRepository.delete(note);
        }catch (Exception exception){
            throw new RepoException("Something went wrong while deleting note.");
        }
    }

    public Map<String, Integer> getStats(String noteId){
        Note note = getNote(noteId);
        if(note.getWordCountMap() != null && !note.getWordCountMap().isEmpty()){
            return note.getWordCountMap();
        }
        return null;
    }
}
