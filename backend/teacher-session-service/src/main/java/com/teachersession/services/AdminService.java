package com.teachersession.services;

import com.teachersession.dto.SessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.entities.enums.UserStatus;
import com.teachersession.mappers.SessionMapper;
import com.teachersession.mappers.UserMapper;
import com.teachersession.repositories.SessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

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

    public List<SessionDto> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalSessions() {
        return sessionRepository.count();
    }
}
