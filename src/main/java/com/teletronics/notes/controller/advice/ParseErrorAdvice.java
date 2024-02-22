package com.teletronics.notes.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.teletronics.notes.enums.Tags;
import com.teletronics.notes.error.FieldErrors;
import com.teletronics.notes.error.GenericError;
import com.teletronics.notes.error.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ParseErrorAdvice extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorDetails = "Unacceptable JSON " + exception.getMessage();

        if (exception.getCause() != null && exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType()!=null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size()-1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
       GenericError errorResponse = new GenericError(new ValidationError(Collections.singletonList(new FieldErrors("tags", "tags must be one of " + Arrays.toString(Tags.values())))));
        return handleExceptionInternal(exception, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                    MethodArgumentNotValidException exception,
                    HttpHeaders httpHeaders, HttpStatus httpStatus,
                    WebRequest webRequest){

        ValidationError validationError = new ValidationError();
        List<FieldErrors> fieldErrors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.add(new FieldErrors(fieldName, errorMessage));
        });
        validationError.setFields(fieldErrors);
        return new ResponseEntity<>(new GenericError(validationError), HttpStatus.BAD_REQUEST);
    }
}
