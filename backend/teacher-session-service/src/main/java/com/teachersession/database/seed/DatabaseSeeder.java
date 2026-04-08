package com.teachersession.database.seed;

import com.teachersession.entities.Enrollment;
import com.teachersession.entities.CourseSession;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.EnrollmentStatus;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.entities.enums.CourseSessionType;
import com.teachersession.repositories.EnrollmentRepository;
import com.teachersession.repositories.CourseSessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.teachersession.database.seed.data.SeedData.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final UserRepository userRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Map<String, Object> seed() {

        log.info("Starting database seeding...");

        var $savedUsers = seedUsers();

        var $savedSessions = seedSessions($savedUsers);

        var $savedEnrollments = seedEnrollments($savedUsers, $savedSessions);

        log.info("Database seeding completed successfully!");

        return Map.of(
                "users", $savedUsers,
                "sessions", $savedSessions,
                "enrollments", $savedEnrollments
        );
    }

    public List<User> seedUsers() {

        List<User> users = new ArrayList<>();

        for (Object[] u : USERS) {

            String email = (String) u[2];

            User user = userRepository.findByEmail(email)
                    .map(existing -> {
                        existing.setFirstName((String) u[0]);
                        existing.setLastName((String) u[1]);
                        existing.setPassword((String) u[3]);
                        existing.setRole((Role) u[4]);
                        return existing;
                    })
                    .orElseGet(() -> User.builder()
                            .firstName((String) u[0])
                            .lastName((String) u[1])
                            .email(email)
                            .password((String) u[3])
                            .role((Role) u[4])
                            .build()
                    );

            users.add(user);
        }

        return userRepository.saveAll(users);

    }

    public List<CourseSession> seedSessions(List<User> users) {

        List<CourseSession> courseSessions = new ArrayList<>();

        for (Object[] s : SESSIONS) {

            User teacher = users.get((Integer) s[0]);
            String title = (String) s[1];

            CourseSession courseSession = courseSessionRepository
                    .findByTitleAndTeacher(title, teacher)
                    .map(existing -> {
                        existing.setDescription((String) s[2]);
                        existing.setSubject((String) s[3]);
                        existing.setPrice(new BigDecimal((String) s[4]));
                        existing.setMaxStudents((Integer) s[5]);
                        existing.setEnrolledStudents((Integer) s[6]);
                        existing.setSessionType((CourseSessionType) s[7]);
                        existing.setMeetingLink((String) s[8]);
                        existing.setLocation((String) s[9]);
                        existing.setStartDate(LocalDateTime.now().plusDays((Integer) s[10]));
                        existing.setDurationMinutes((Integer) s[11]);
                        existing.setStatus((CourseSessionStatus) s[12]);
                        return existing;
                    })
                    .orElseGet(() -> CourseSession.builder()
                            .teacher(teacher)
                            .title(title)
                            .description((String) s[2])
                            .subject((String) s[3])
                            .price(new BigDecimal((String) s[4]))
                            .maxStudents((Integer) s[5])
                            .enrolledStudents((Integer) s[6])
                            .sessionType((CourseSessionType) s[7])
                            .meetingLink((String) s[8])
                            .location((String) s[9])
                            .startDate(LocalDateTime.now().plusDays((Integer) s[10]))
                            .durationMinutes((Integer) s[11])
                            .status((CourseSessionStatus) s[12])
                            .build()
                    );

            courseSessions.add(courseSession);
        }

        return courseSessionRepository.saveAll(courseSessions);
    }

    public List<Enrollment> seedEnrollments(List<User> users, List<CourseSession> courseSessions) {

        List<Enrollment> enrollments = new ArrayList<>();

        for (Object[] e : ENROLLMENTS) {

            CourseSession courseSession = courseSessions.get((Integer) e[0]);
            User student = users.get((Integer) e[1]);

            Enrollment enrollment = enrollmentRepository
                    .findBySessionAndStudent(courseSession, student)
                    .map(existing -> {
                        existing.setStatus((EnrollmentStatus) e[2]);
                        return existing;
                    })
                    .orElseGet(() -> Enrollment.builder()
                            .session(courseSession)
                            .student(student)
                            .status((EnrollmentStatus) e[2])
                            .build()
                    );

            enrollments.add(enrollment);
        }

        return enrollmentRepository.saveAll(enrollments);
    }
}