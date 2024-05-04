package com.example.backend.exception;

public class TimeSheetAlreadyApproved extends RuntimeException {

    public TimeSheetAlreadyApproved(String message) {
        super(message);
    }

    public TimeSheetAlreadyApproved(String message, Throwable cause) {
        super(message, cause);
    }
}
