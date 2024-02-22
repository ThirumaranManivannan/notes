package com.teletronics.notes.exceptions;

public class NotesNotFoundException extends RuntimeException{
    public NotesNotFoundException(String message){
        super(message);
    }
}
