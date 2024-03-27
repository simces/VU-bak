package com.photo.business.handlers.exceptions;

public class PasswordConfirmationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "New password and confirm new password do not match";

    public PasswordConfirmationException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordConfirmationException(String message) {
        super(message);
    }
}