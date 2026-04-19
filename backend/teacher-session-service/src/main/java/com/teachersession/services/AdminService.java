package com.teachersession.services;

import com.teachersession.dto.CourseSessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.BugLog;
import com.teachersession.entities.CourseSession;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.entities.enums.UserStatus;
import com.teachersession.mappers.CourseSessionMapper;
import com.teachersession.mappers.UserMapper;
import com.teachersession.repositories.BugLogRepository;
import com.teachersession.repositories.CourseSessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final BugLogRepository bugLogRepository;
    private final UserMapper userMapper;
    private final CourseSessionMapper courseSessionMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    public List<CourseSessionDto> getAllSessions() {
        return courseSessionRepository.findAll().stream()
                .map(courseSessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelSession(Long sessionId) {
        CourseSession courseSession = courseSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        courseSession.setStatus(CourseSessionStatus.CANCELLED);
        courseSessionRepository.save(courseSession);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalSessions() {
        return courseSessionRepository.count();
    }

    public long getTotalBugLogs() {
        return bugLogRepository.count();
    }

    public List<BugLog> getAllBugLogs() {
        return bugLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public BugLog getBugLogById(Long id) {
        return bugLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Buglog not found"));
    }
}
