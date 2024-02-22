package com.teletronics.notes.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError implements Error{
    private final Integer errorCode = 4003;
    private final String type = "Validation Error";
    private List<FieldErrors> fields;
}
