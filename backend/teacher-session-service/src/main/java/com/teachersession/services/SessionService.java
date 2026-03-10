package com.teachersession.services;

import com.teachersession.dto.SessionDto;
import com.teachersession.entities.Session;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.SessionStatus;
import com.teachersession.entities.enums.SessionType;
import com.teachersession.exceptions.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                
        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Only teachers can create sessions");
        }
        
        if (dto.getSessionType() == SessionType.ONLINE && (dto.getMeetingLink() == null || dto.getMeetingLink().isEmpty())) {
            throw new IllegalArgumentException("Online sessions require a meeting link");
        }
        
        if (dto.getSessionType() == SessionType.OFFLINE && (dto.getLocation() == null || dto.getLocation().isEmpty())) {
            throw new IllegalArgumentException("Offline sessions require a location");
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

    public SessionDto getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }
    
    public List<SessionDto> getSessionsByTeacher(Long teacherId) {
        return sessionRepository.findByTeacherId(teacherId).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SessionDto cancelSession(Long sessionId, Long teacherId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
                
        if (!session.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Not authorized to cancel this session");
        }
        
        if (session.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot cancel a session that has already started");
        }
        
        session.setStatus(SessionStatus.CANCELLED);
        return sessionMapper.toDto(sessionRepository.save(session));
    }
}
