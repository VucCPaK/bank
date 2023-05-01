package com.example.back.exceptions;

public class InvalidEventTypeException extends RuntimeException {
    public InvalidEventTypeException(String eventType) {
        super("Invalid event type: " + eventType);
    }
}
