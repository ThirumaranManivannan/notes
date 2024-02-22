package com.teletronics.notes.controller.advice;

import com.teletronics.notes.error.*;
import com.teletronics.notes.exceptions.NoteIdInvalidException;
import com.teletronics.notes.exceptions.NotesNotFoundException;
import com.teletronics.notes.exceptions.RepoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class CommonControllerAdvice {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, Object> errors = new HashMap<>();
//        ValidationError validationError = new ValidationError();
//        List<FieldErrors> fieldErrors = new ArrayList<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            fieldErrors.add(new FieldErrors(fieldName, errorMessage));
//        });
//        validationError.setFields(fieldErrors);
//        log.error("returning this.....");
//        return new ResponseEntity<>(new GenericError(validationError), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(RepoException.class)
    public ResponseEntity<?> handleRepoException(RepoException ex) {
        return new ResponseEntity<>(new GenericError(new CommonError(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotesNotFoundException.class)
    public ResponseEntity<?> handleNotesNotFoundException(NotesNotFoundException ex) {
        return new ResponseEntity<>(new GenericError(new NotFoundError(ex.getMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoteIdInvalidException.class)
    public ResponseEntity<?> handleNoteIdInvalidException(NoteIdInvalidException ex) {
        return new ResponseEntity<>(new GenericError(new ValidationError(Collections.singletonList(new FieldErrors("noteId", ex.getMessage())))), HttpStatus.NOT_FOUND);
    }
}
