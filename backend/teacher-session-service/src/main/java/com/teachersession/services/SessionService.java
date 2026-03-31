package com.teachersession.services;

import com.teachersession.dto.SessionDto;
import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.entities.enums.SessionType;
import com.teachersession.exceptions.SessionException;
import com.teachersession.exceptions.enums.SessionErrorCode;
import com.teachersession.mappers.SessionMapper;
import com.teachersession.repositories.SessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    @Transactional
    public SessionDto createSession(SessionDto dto) {
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new SessionException(SessionErrorCode.TEACHER_NOT_FOUND));
                
        if (teacher.getRole() != Role.TEACHER) {
            throw new SessionException(SessionErrorCode.ONLY_TEACHERS_CAN_CREATE_SESSIONS);
        }
        
        if (dto.getSessionType() == SessionType.ONLINE && (dto.getMeetingLink() == null || dto.getMeetingLink().isEmpty())) {
            throw new SessionException(SessionErrorCode.ONLINE_SESSION_REQUIRES_MEETING_LINK);
        }
        
        if (dto.getSessionType() == SessionType.OFFLINE && (dto.getLocation() == null || dto.getLocation().isEmpty())) {
            throw new SessionException(SessionErrorCode.OFFLINE_SESSION_REQUIRES_LOCATION);
        }

        Session session = Session.builder()
                .teacher(teacher)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .subject(dto.getSubject())
                .price(dto.getPrice())
                .maxStudents(dto.getMaxStudents())
                .sessionType(dto.getSessionType())
                .location(dto.getLocation())
                .meetingLink(dto.getMeetingLink())
                .startDate(dto.getStartDate())
                .durationMinutes(dto.getDurationMinutes())
                .status(SessionStatus.OPEN)
                .build();

        return sessionMapper.toDto(sessionRepository.save(session));
    }

    public List<SessionDto> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SessionDto> searchSessions(String search, SessionType type) {
        String finalSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return sessionRepository.searchSessions(finalSearch, type).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public SessionDto getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toDto)
                .orElseThrow(() -> new SessionException(SessionErrorCode.SESSION_NOT_FOUND));
    }
    
    public List<SessionDto> getSessionsByTeacher(Long teacherId) {
        return sessionRepository.findByTeacherId(teacherId).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SessionDto cancelSession(Long sessionId, Long teacherId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException(SessionErrorCode.SESSION_NOT_FOUND));
                
        if (!session.getTeacher().getId().equals(teacherId)) {
            throw new SessionException(SessionErrorCode.NOT_AUTHORIZED_TO_CANCEL);
        }
        
        if (session.getStartDate().isBefore(LocalDateTime.now())) {
            throw new SessionException(SessionErrorCode.CANNOT_CANCEL_STARTED_SESSION);
        }
        
        session.setStatus(SessionStatus.CANCELLED);
        return sessionMapper.toDto(sessionRepository.save(session));
    }
}
