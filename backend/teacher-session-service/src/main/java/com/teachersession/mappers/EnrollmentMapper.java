package com.teachersession.mappers;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.entities.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentDto toDto(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        String  sessionTitle = null;
        Long sessionId = null;
        if (enrollment.getSession() != null) {
            sessionTitle = enrollment.getSession().getTitle();
            sessionId = enrollment.getSession().getId();
        }

        String studentName = null;
        Long studentId = null;
        if (enrollment.getStudent() != null) {
            studentName = enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName();
            studentId = enrollment.getStudent().getId();
        }

        return EnrollmentDto.builder()
                .id(enrollment.getId())
                .sessionId(sessionId)
                .sessionTitle(sessionTitle)
                .studentId(studentId)
                .studentName(studentName)
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}
