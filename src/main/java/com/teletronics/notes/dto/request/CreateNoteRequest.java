package com.teletronics.notes.dto.request;

import com.teletronics.notes.annotations.ValidTags;
import com.teletronics.notes.enums.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoteRequest {
    @NotBlank(message = "Title must be provided")
    private String title;
    @NotBlank(message = "Text must be provided")
    private String text;
    @ValidTags(enumClass = Tags.class)
    private Set<Tags> tags;
}
