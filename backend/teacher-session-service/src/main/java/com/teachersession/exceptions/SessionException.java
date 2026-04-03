package com.teachersession.exceptions;

import com.teachersession.exceptions.enums.SessionErrorCode;

public class SessionException extends RuntimeException {

    private final SessionErrorCode errorCode;

    public SessionException(SessionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SessionErrorCode getErrorCode() {
        return errorCode;
    }
}
