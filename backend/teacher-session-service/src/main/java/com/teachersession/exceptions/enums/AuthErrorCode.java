package com.teachersession.exceptions.enums;

public enum AuthErrorCode {

    EMAIL_NOT_FOUND("Email not found"),
    WRONG_PASSWORD("Wrong password"),
    EMAIL_ALREADY_EXISTS("Email already in use");

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}