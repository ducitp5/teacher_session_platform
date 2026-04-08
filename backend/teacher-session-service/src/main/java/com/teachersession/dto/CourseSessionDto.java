package com.teachersession.dto;

import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.entities.enums.CourseSessionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSessionDto {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String title;
    private String description;
    private String subject;
    private BigDecimal price;
    private Integer maxStudents;
    private Integer enrolledStudents;
    private CourseSessionType sessionType;
    private String location;
    private String meetingLink;
    private LocalDateTime startDate;
    private Integer durationMinutes;
    private CourseSessionStatus status;
}
