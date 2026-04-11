package com.teachersession.exceptions;

import com.teachersession.exceptions.enums.CourseSessionErrorCode;

public class CourseSessionException extends RuntimeException {

    private final CourseSessionErrorCode errorCode;

    public CourseSessionException(CourseSessionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CourseSessionErrorCode getErrorCode() {
        return errorCode;
    }
}
