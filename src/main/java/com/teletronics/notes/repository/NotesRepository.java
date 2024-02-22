package com.teletronics.notes.repository;

import com.teletronics.notes.document.Note;
import com.teletronics.notes.enums.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface NotesRepository extends MongoRepository<Note, String> {
    Page<Note> findByTagsIn(List<Tags> tags, Pageable pageable);
}
