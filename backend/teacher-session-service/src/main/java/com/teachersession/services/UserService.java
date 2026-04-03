package com.teachersession.services;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UserService {
    protected final SessionService sessionService;
    protected final EnrollmentService enrollmentService;
}
