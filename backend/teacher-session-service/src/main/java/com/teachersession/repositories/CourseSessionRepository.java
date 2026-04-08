package com.teachersession.repositories;

import com.teachersession.entities.CourseSession;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.CourseSessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    List<CourseSession> findByTeacherId(Long teacherId);
    List<CourseSession> findBySubjectContainingIgnoreCase(String subject);
    List<CourseSession> findBySessionType(CourseSessionType sessionType);

    @Query("SELECT s FROM CourseSession s WHERE (:search IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:type IS NULL OR s.sessionType = :type)")
    List<CourseSession> searchSessions(@Param("search") String search, @Param("type") CourseSessionType type);

    Optional<CourseSession> findByTitleAndTeacher(String title, User teacher);
}
