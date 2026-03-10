package com.teachersession.dto;

import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.entities.enums.SessionType;
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
public class SessionDto {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String title;
    private String description;
    private String subject;
    private BigDecimal price;
    private Integer maxStudents;
    private Integer enrolledStudents;
    private SessionType sessionType;
    private String location;
    private String meetingLink;
    private LocalDateTime startDate;
    private Integer durationMinutes;
    private SessionStatus status;
}
