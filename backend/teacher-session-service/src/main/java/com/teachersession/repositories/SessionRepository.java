package com.teachersession.repositories;

import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTeacherId(Long teacherId);
    List<Session> findBySubjectContainingIgnoreCase(String subject);
    List<Session> findBySessionType(SessionType sessionType);

    Optional<Session> findByTitleAndTeacher(String title, User teacher);
}
