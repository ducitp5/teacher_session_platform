package com.teachersession.services;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UserService {
    protected final CourseSessionService courseSessionService;
    protected final EnrollmentService enrollmentService;
}
