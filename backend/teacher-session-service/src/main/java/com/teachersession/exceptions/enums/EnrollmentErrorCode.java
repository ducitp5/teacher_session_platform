package com.teachersession.exceptions.enums;

import lombok.Getter;

@Getter
public enum EnrollmentErrorCode {

    SESSION_NOT_FOUND("Session not found"),
    STUDENT_NOT_FOUND("Student not found"),
    ONLY_STUDENTS_CAN_ENROLL("Only students can enroll in sessions"),
    SESSION_NOT_OPEN("Session is not open for enrollment"),
    ALREADY_ENROLLED("Student is already enrolled in this session"),
    ENROLLMENT_NOT_FOUND("Enrollment not found"),
    NOT_AUTHORIZED_TO_CANCEL("Not authorized to cancel this enrollment");

    private final String message;

    EnrollmentErrorCode(String message) {
        this.message = message;
    }

}
