package com.teachersession.exceptions;

import com.teachersession.exceptions.enums.EnrollmentErrorCode;

public class EnrollmentException extends RuntimeException {

    private final EnrollmentErrorCode errorCode;

    public EnrollmentException(EnrollmentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public EnrollmentErrorCode getErrorCode() {
        return errorCode;
    }
}
