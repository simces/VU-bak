package com.photo.business.handlers.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Username already taken";

    public UsernameAlreadyTakenException() {
        super(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
