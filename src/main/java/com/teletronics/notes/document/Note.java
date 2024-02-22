package com.teletronics.notes.document;

import com.teletronics.notes.dto.request.CreateNoteRequest;
import com.teletronics.notes.dto.request.UpdateNoteRequest;
import com.teletronics.notes.enums.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    @Indexed
    private String title;
    private String text;
    @Indexed
    private LocalDateTime creationTime;
    private LocalDateTime updatedTime;
    private Set<Tags> tags;
    private Map<String, Integer> wordCountMap;


    public Note(CreateNoteRequest createNoteRequest){
        this.title = createNoteRequest.getTitle();
        this.text = createNoteRequest.getText();
        this.creationTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        if(createNoteRequest.getTags() != null && !createNoteRequest.getTags().isEmpty())
            this.tags = createNoteRequest.getTags();
        else
            this.tags = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id.equals(note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
