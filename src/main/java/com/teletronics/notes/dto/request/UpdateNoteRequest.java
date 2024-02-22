package com.teletronics.notes.dto.request;

import com.teletronics.notes.annotations.ValidTags;
import com.teletronics.notes.enums.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {
    private String title;
    private String text;
    @ValidTags(enumClass = Tags.class)
    private Set<Tags> tags;
}
