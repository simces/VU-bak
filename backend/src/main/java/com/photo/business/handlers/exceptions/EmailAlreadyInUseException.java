package com.photo.business.handlers.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Email already in use";

    public EmailAlreadyInUseException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
