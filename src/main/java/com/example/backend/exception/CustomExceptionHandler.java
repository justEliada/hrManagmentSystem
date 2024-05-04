package com.example.backend.exception;

public class CustomExceptionHandler extends RuntimeException {

    public CustomExceptionHandler(String message) {
        super(message);
    }

    public CustomExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}