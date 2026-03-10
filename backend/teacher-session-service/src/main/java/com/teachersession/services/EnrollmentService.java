package com.teachersession.services;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.entities.Enrollment;
import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.EnrollmentStatus;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.exceptions.ResourceNotFoundException;
import com.teachersession.mappers.EnrollmentMapper;
import com.teachersession.repositories.EnrollmentRepository;
import com.teachersession.repositories.SessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public EnrollmentDto enrollStudent(Long sessionId, Long studentId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
                
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
                
        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Only students can enroll in sessions");
        }
        
        if (session.getStatus() != SessionStatus.OPEN) {
            throw new IllegalArgumentException("Session is not open for enrollment");
        }
        
        if (enrollmentRepository.findBySessionIdAndStudentId(sessionId, studentId).isPresent()) {
            throw new IllegalArgumentException("Student is already enrolled in this session");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .session(session)
                .student(student)
                .status(EnrollmentStatus.ACTIVE)
                .build();
                
        session.setEnrolledStudents(session.getEnrolledStudents() + 1);
        
        if (session.getEnrolledStudents() >= session.getMaxStudents()) {
            session.setStatus(SessionStatus.FULL);
        }
        sessionRepository.save(session);
        
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentDto> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelEnrollment(Long enrollmentId, Long studentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
                
        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Not authorized to cancel this enrollment");
        }
        
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
        
        Session session = enrollment.getSession();
        session.setEnrolledStudents(session.getEnrolledStudents() - 1);
        if (session.getStatus() == SessionStatus.FULL && session.getEnrolledStudents() < session.getMaxStudents()) {
            session.setStatus(SessionStatus.OPEN);
        }
        sessionRepository.save(session);
    }
}
