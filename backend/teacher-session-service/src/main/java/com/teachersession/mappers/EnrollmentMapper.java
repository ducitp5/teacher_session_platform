package com.teachersession.mappers;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.entities.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentDto toDto(Enrollment entity) {
        if (entity == null) {
            return null;
        }

        String  sessionTitle = null;
        Long sessionId = null;
        if (entity.getSession() != null) {
            sessionTitle = entity.getSession().getTitle();
            sessionId = entity.getSession().getId();
        }

        String studentName = null;
        Long studentId = null;
        if (entity.getStudent() != null) {
            studentName = entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName();
            studentId = entity.getStudent().getId();
        }

        return EnrollmentDto.builder()
                .id(entity.getId())
                .sessionId(sessionId)
                .sessionTitle(sessionTitle)
                .studentId(studentId)
                .studentName(studentName)
                .status(entity.getStatus())
                .enrolledAt(entity.getEnrolledAt())
                .build();
    }
}
