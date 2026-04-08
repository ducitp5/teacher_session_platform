package com.teachersession.services;

import org.springframework.stereotype.Service;

@Service
public class StudentService extends UserService {
    
    public StudentService(CourseSessionService courseSessionService, EnrollmentService enrollmentService) {
        super(courseSessionService, enrollmentService);
    }
    
    // Student specific operations
}
