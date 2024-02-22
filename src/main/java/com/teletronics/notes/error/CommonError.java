package com.teletronics.notes.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonError implements Error{
    private Integer errorCode = 5000;
    private String type = "Server Error";
    private String message;
    public CommonError(String message){
        this.message = message;
    }
}
