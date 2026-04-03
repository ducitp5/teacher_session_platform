package com.teachersession.services;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.dto.SessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService extends UserService {

    public TeacherService(SessionService sessionService, EnrollmentService enrollmentService) {
        super(sessionService, enrollmentService);
    }

    public List<SessionDto> getTeacherSessions(Long teacherId) {
        return sessionService.getSessionsByTeacher(teacherId);
    }

    public void createTeacherSession(SessionDto sessionDto, Long teacherId) {
        sessionDto.setTeacherId(teacherId);
        sessionService.createSession(sessionDto);
    }

    public void cancelTeacherSession(Long sessionId, Long teacherId) {
        sessionService.cancelSession(sessionId, teacherId);
    }

    public SessionDto getSessionById(Long sessionId) {
        return sessionService.getSessionById(sessionId);
    }

    public List<EnrollmentDto> getSessionEnrollments(Long sessionId) {
        return enrollmentService.getSessionEnrollments(sessionId);
    }

    public void updateTeacherSession(SessionDto sessionDto, Long teacherId) {
        sessionService.updateSession(sessionDto, teacherId);
    }
}
