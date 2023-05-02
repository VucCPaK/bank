package com.example.back.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String id) {
        super("Could not find card with aggregate id: " + id);
    }
}
