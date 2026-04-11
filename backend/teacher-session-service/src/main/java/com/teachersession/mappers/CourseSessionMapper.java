package com.teachersession.mappers;

import com.teachersession.dto.CourseSessionDto;
import com.teachersession.entities.CourseSession;
import org.springframework.stereotype.Component;

@Component
public class CourseSessionMapper {

    public CourseSessionDto toDto(CourseSession entity) {
        if (entity == null) {
            return null;
        }

        String teacherName = null;
        Long teacherId = null;
        if (entity.getTeacher() != null) {
            teacherName = entity.getTeacher().getFirstName() + " " + entity.getTeacher().getLastName();
            teacherId = entity.getTeacher().getId();
        }

        return CourseSessionDto.builder()
                .id(entity.getId())
                .teacherId(teacherId)
                .teacherName(teacherName)
                .title(entity.getTitle())
                .description(entity.getDescription())
                .subject(entity.getSubject())
                .price(entity.getPrice())
                .maxStudents(entity.getMaxStudents())
                .enrolledStudents(entity.getEnrolledStudents())
                .sessionType(entity.getSessionType())
                .location(entity.getLocation())
                .meetingLink(entity.getMeetingLink())
                .startDate(entity.getStartDate())
                .durationMinutes(entity.getDurationMinutes())
                .status(entity.getStatus())
                .build();
    }
}
