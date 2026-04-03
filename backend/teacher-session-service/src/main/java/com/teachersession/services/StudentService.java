package com.teachersession.services;

import org.springframework.stereotype.Service;

@Service
public class StudentService extends UserService {
    
    public StudentService(SessionService sessionService, EnrollmentService enrollmentService) {
        super(sessionService, enrollmentService);
    }
    
    // Student specific operations
}
