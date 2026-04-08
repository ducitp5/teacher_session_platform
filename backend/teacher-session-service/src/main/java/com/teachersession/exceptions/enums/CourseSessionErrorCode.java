package com.teachersession.exceptions.enums;

import lombok.Getter;

@Getter
public enum CourseSessionErrorCode {

    TEACHER_NOT_FOUND("Teacher not found"),
    ONLY_TEACHERS_CAN_CREATE_SESSIONS("Only teachers can create sessions"),
    ONLINE_SESSION_REQUIRES_MEETING_LINK("Online sessions require a meeting link"),
    OFFLINE_SESSION_REQUIRES_LOCATION("Offline sessions require a location"),
    SESSION_NOT_FOUND("Session not found"),
    NOT_AUTHORIZED_TO_CANCEL("Not authorized to cancel this session"),
    CANNOT_CANCEL_STARTED_SESSION("Cannot cancel a session that has already started"),
    CANNOT_UPDATE_STARTED_SESSION("Cannot update a session that has already started");

    private final String message;

    CourseSessionErrorCode(String message) {
        this.message = message;
    }

}
