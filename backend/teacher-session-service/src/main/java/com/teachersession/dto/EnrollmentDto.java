package com.teachersession.dto;

import com.teachersession.entities.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private Long studentId;
    private String studentName;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
}
