package com.example.myspringbootprojectsession12.exceptions;

public class ForbiddenFieldException extends RuntimeException {
    public ForbiddenFieldException(String message) {
        super(message);
    }
}
