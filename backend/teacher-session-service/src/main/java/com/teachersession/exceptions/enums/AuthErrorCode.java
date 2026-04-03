package com.teachersession.exceptions.enums;

import lombok.Getter;

@Getter
public enum AuthErrorCode {

    EMAIL_NOT_FOUND("Email not found"),
    WRONG_PASSWORD("Wrong password"),
    EMAIL_ALREADY_EXISTS("Email already in use");

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

}