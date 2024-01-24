package com.photo.business.handlers.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Incorrect current password";

    public IncorrectPasswordException() {
        super(DEFAULT_MESSAGE);
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
