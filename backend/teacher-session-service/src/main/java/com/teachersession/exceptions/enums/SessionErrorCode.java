package com.teachersession.exceptions.enums;

import lombok.Getter;

@Getter
public enum SessionErrorCode {

    TEACHER_NOT_FOUND("Teacher not found"),
    ONLY_TEACHERS_CAN_CREATE_SESSIONS("Only teachers can create sessions"),
    ONLINE_SESSION_REQUIRES_MEETING_LINK("Online sessions require a meeting link"),
    OFFLINE_SESSION_REQUIRES_LOCATION("Offline sessions require a location"),
    SESSION_NOT_FOUND("Session not found"),
    NOT_AUTHORIZED_TO_CANCEL("Not authorized to cancel this session"),
    CANNOT_CANCEL_STARTED_SESSION("Cannot cancel a session that has already started");

    private final String message;

    SessionErrorCode(String message) {
        this.message = message;
    }

}
