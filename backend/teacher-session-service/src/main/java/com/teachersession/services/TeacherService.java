package com.teachersession.services;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.dto.CourseSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService extends UserService {

    public TeacherService(CourseSessionService courseSessionService, EnrollmentService enrollmentService) {
        super(courseSessionService, enrollmentService);
    }

    public List<CourseSessionDto> getTeacherSessions(Long teacherId) {
        return courseSessionService.getSessionsByTeacher(teacherId);
    }

    public void createTeacherSession(CourseSessionDto courseSessionDto, Long teacherId) {
        courseSessionDto.setTeacherId(teacherId);
        courseSessionService.createSession(courseSessionDto);
    }

    public void cancelTeacherSession(Long sessionId, Long teacherId) {
        courseSessionService.cancelSession(sessionId, teacherId);
    }

    public CourseSessionDto getSessionById(Long sessionId) {
        return courseSessionService.getSessionById(sessionId);
    }

    public List<EnrollmentDto> getSessionEnrollments(Long sessionId) {
        return enrollmentService.getSessionEnrollments(sessionId);
    }

    public void updateTeacherSession(CourseSessionDto courseSessionDto, Long teacherId) {
        courseSessionService.updateSession(courseSessionDto, teacherId);
    }
}
