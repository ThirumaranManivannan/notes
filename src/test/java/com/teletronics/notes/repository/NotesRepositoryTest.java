package com.teletronics.notes.repository;

import com.teletronics.notes.document.Note;
import com.teletronics.notes.enums.Tags;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class NotesRepositoryTest {

    @Autowired
    private NotesRepository notesRepository;


    @BeforeEach
    void setUp() {

    }

    @Test
    public void findByTagsInTest(){

        Set<Tags> tags = new HashSet<>();
        tags.add(Tags.IMPORTANT);
        Note note1 = new Note("note-1", "Physics",
                "This is physics",
                LocalDateTime.now(), LocalDateTime.now(), tags, null);
        notesRepository.save(note1);

        tags = new HashSet<>();
        tags.add(Tags.BUSINESS);tags.add(Tags.PERSONAL);
        Note note2 = new Note("note-2", "Maths",
                "This is maths",
                LocalDateTime.now(), LocalDateTime.now(), tags, null);
        notesRepository.save(note2);

        tags = new HashSet<>();
        tags.add(Tags.BUSINESS);tags.add(Tags.PERSONAL);
        Note note3 = new Note("note-3", "English",
                "This is English",
                LocalDateTime.now(), LocalDateTime.now(), tags, null);
        notesRepository.save(note3);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationTime"));
        List<Tags> tagsQuery = Arrays.asList(Tags.IMPORTANT, Tags.BUSINESS);
        Page<Note> pageOfNotes = notesRepository.findByTagsIn(tagsQuery, pageRequest);

        assertThat(pageOfNotes.getTotalPages()).isEqualTo(1);
        assertThat(pageOfNotes.getTotalElements()).isEqualTo(3);
        assertThat(pageOfNotes.getNumber()).isEqualTo(0);
        List<Note> notes = pageOfNotes.getContent();
        assertThat(notes.get(0)).isNotNull().isEqualTo(note3);
        assertThat(notes.get(1)).isNotNull().isEqualTo(note2);
        assertThat(notes.get(2)).isNotNull().isEqualTo(note1);

    }

    @AfterEach
    void tearDown() {

    }
}