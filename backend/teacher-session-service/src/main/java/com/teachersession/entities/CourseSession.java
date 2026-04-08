package com.teachersession.entities;

import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.entities.enums.CourseSessionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Column(name = "enrolled_students")
    @Builder.Default
    private Integer enrolledStudents = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    private CourseSessionType sessionType;

    private String location;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CourseSessionStatus status = CourseSessionStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
