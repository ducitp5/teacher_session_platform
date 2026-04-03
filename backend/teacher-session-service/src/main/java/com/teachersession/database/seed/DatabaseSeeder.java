package com.teachersession.database.seed;

import com.teachersession.entities.Enrollment;
import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.EnrollmentStatus;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.entities.enums.SessionType;
import com.teachersession.repositories.EnrollmentRepository;
import com.teachersession.repositories.SessionRepository;
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
    private final SessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Map<String, Object> seed() {

        log.info("Starting database seeding...");

        /*
         * USERS
         */
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

        var $savedUsers = userRepository.saveAll(users);

        /*
         * SESSIONS
         */
        List<Session> sessions = new ArrayList<>();

        for (Object[] s : SESSIONS) {

            User teacher = users.get((Integer) s[0]);
            String title = (String) s[1];

            Session session = sessionRepository
                    .findByTitleAndTeacher(title, teacher)
                    .map(existing -> {
                        existing.setDescription((String) s[2]);
                        existing.setSubject((String) s[3]);
                        existing.setPrice(new BigDecimal((String) s[4]));
                        existing.setMaxStudents((Integer) s[5]);
                        existing.setEnrolledStudents((Integer) s[6]);
                        existing.setSessionType((SessionType) s[7]);
                        existing.setMeetingLink((String) s[8]);
                        existing.setLocation((String) s[9]);
                        existing.setStartDate(LocalDateTime.now().plusDays((Integer) s[10]));
                        existing.setDurationMinutes((Integer) s[11]);
                        existing.setStatus((SessionStatus) s[12]);
                        return existing;
                    })
                    .orElseGet(() -> Session.builder()
                            .teacher(teacher)
                            .title(title)
                            .description((String) s[2])
                            .subject((String) s[3])
                            .price(new BigDecimal((String) s[4]))
                            .maxStudents((Integer) s[5])
                            .enrolledStudents((Integer) s[6])
                            .sessionType((SessionType) s[7])
                            .meetingLink((String) s[8])
                            .location((String) s[9])
                            .startDate(LocalDateTime.now().plusDays((Integer) s[10]))
                            .durationMinutes((Integer) s[11])
                            .status((SessionStatus) s[12])
                            .build()
                    );

            sessions.add(session);
        }

        var $savedSessions = sessionRepository.saveAll(sessions);

        /*
         * ENROLLMENTS
         */
        List<Enrollment> enrollments = new ArrayList<>();

        for (Object[] e : ENROLLMENTS) {

            Session session = sessions.get((Integer) e[0]);
            User student = users.get((Integer) e[1]);

            Enrollment enrollment = enrollmentRepository
                    .findBySessionAndStudent(session, student)
                    .map(existing -> {
                        existing.setStatus((EnrollmentStatus) e[2]);
                        return existing;
                    })
                    .orElseGet(() -> Enrollment.builder()
                            .session(session)
                            .student(student)
                            .status((EnrollmentStatus) e[2])
                            .build()
                    );

            enrollments.add(enrollment);
        }

        var $savedEnrollments = enrollmentRepository.saveAll(enrollments);

        log.info("Database seeding completed successfully!");

        return Map.of(
                "users", $savedUsers,
                "sessions", $savedSessions,
                "enrollments", $savedEnrollments
        );
    }
}