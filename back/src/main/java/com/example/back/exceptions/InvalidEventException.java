package com.example.back.exceptions;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String message) {
        super("Invalid event: " + message);
    }
}
