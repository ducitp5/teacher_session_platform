package com.teachersession.repositories;

import com.teachersession.entities.BugLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugLogRepository extends JpaRepository<BugLog, Long> {
}
