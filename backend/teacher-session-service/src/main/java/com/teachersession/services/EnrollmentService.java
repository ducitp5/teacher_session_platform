package com.teachersession.services;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.entities.Enrollment;
import com.teachersession.entities.CourseSession;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.EnrollmentStatus;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.exceptions.EnrollmentException;
import com.teachersession.exceptions.enums.EnrollmentErrorCode;
import com.teachersession.mappers.EnrollmentMapper;
import com.teachersession.repositories.EnrollmentRepository;
import com.teachersession.repositories.CourseSessionRepository;
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
    private final CourseSessionRepository courseSessionRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public EnrollmentDto enrollStudent(Long sessionId, Long studentId) {
        CourseSession courseSession = courseSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.SESSION_NOT_FOUND));
                
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.STUDENT_NOT_FOUND));
                
        if (student.getRole() != Role.STUDENT) {
            throw new EnrollmentException(EnrollmentErrorCode.ONLY_STUDENTS_CAN_ENROLL);
        }
        
        if (courseSession.getStatus() != CourseSessionStatus.OPEN) {
            throw new EnrollmentException(EnrollmentErrorCode.SESSION_NOT_OPEN);
        }
        
        if (enrollmentRepository.findBySessionIdAndStudentId(sessionId, studentId).isPresent()) {
            throw new EnrollmentException(EnrollmentErrorCode.ALREADY_ENROLLED);
        }
        
        Enrollment enrollment = Enrollment.builder()
                .session(courseSession)
                .student(student)
                .status(EnrollmentStatus.ACTIVE)
                .build();
                
        courseSession.setEnrolledStudents(courseSession.getEnrolledStudents() + 1);
        
        if (courseSession.getEnrolledStudents() >= courseSession.getMaxStudents()) {
            courseSession.setStatus(CourseSessionStatus.FULL);
        }
        courseSessionRepository.save(courseSession);
        
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentDto> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getSessionEnrollments(Long sessionId) {
        return enrollmentRepository.findBySessionId(sessionId).stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean isStudentEnrolled(Long sessionId, Long studentId) {
        return enrollmentRepository.findBySessionIdAndStudentId(sessionId, studentId)
                .map(enrollment -> enrollment.getStatus() == EnrollmentStatus.ACTIVE)
                .orElse(false);
    }

    @Transactional
    public void cancelEnrollment(Long enrollmentId, Long studentId) {
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND));
                
        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new EnrollmentException(EnrollmentErrorCode.NOT_AUTHORIZED_TO_CANCEL);
        }
        
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
        
        CourseSession courseSession = enrollment.getSession();
        courseSession.setEnrolledStudents(courseSession.getEnrolledStudents() - 1);
        if (courseSession.getStatus() == CourseSessionStatus.FULL && courseSession.getEnrolledStudents() < courseSession.getMaxStudents()) {
            courseSession.setStatus(CourseSessionStatus.OPEN);
        }
        courseSessionRepository.save(courseSession);
    }
}
