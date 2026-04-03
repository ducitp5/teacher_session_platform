package com.teachersession.repositories;

import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTeacherId(Long teacherId);
    List<Session> findBySubjectContainingIgnoreCase(String subject);
    List<Session> findBySessionType(SessionType sessionType);

    @Query("SELECT s FROM Session s WHERE (:search IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:type IS NULL OR s.sessionType = :type)")
    List<Session> searchSessions(@Param("search") String search, @Param("type") SessionType type);

    Optional<Session> findByTitleAndTeacher(String title, User teacher);
}
