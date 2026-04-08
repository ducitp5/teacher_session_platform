package com.teachersession.database.seed.data;

import com.teachersession.entities.enums.*;

public class SeedData {

    public static final Object[][] USERS = {
            {"Demo", "Teacher", "teacher@demo.com", "password123", Role.TEACHER},
            {"Demo", "Student", "student@demo.com", "password123", Role.STUDENT},
            {"Jane", "Doe", "jane.doe@demo.com", "password123", Role.STUDENT},
            {"System", "Admin", "admin@demo.com", "password123", Role.ADMIN}
    };

    public static final Object[][] SESSIONS = {
            {
                    0,
                    "Advanced Java Programming",
                    "Deep dive into Java concurrency",
                    "Computer Science",
                    "99.99",
                    20,
                    2,
                    SessionType.ONLINE,
                    "https://zoom.us/j/demo123",
                    null,
                    3,
                    120,
                    SessionStatus.OPEN
            },
            {
                    0,
                    "Creative Writing Workshop",
                    "Learn storytelling",
                    "Literature",
                    "49.99",
                    10,
                    1,
                    SessionType.OFFLINE,
                    null,
                    "Central Library, Room 101",
                    5,
                    90,
                    SessionStatus.OPEN
            }
    };

    public static final Object[][] ENROLLMENTS = {
            {0,1,EnrollmentStatus.ACTIVE},
            {0,2,EnrollmentStatus.ACTIVE},
            {1,1,EnrollmentStatus.ACTIVE}
    };

}