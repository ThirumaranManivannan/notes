package com.teletronics.notes.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotFoundError implements Error{
    private Integer errorCode = 4004;
    private String type = "Not Found";
    private String message;
    public NotFoundError(String message){
        this.message = message;
    }
}
