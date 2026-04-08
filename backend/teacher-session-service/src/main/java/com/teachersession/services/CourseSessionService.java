package com.teachersession.services;

import com.teachersession.dto.CourseSessionDto;
import com.teachersession.entities.CourseSession;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.CourseSessionStatus;
import com.teachersession.entities.enums.CourseSessionType;
import com.teachersession.exceptions.CourseSessionException;
import com.teachersession.exceptions.enums.CourseSessionErrorCode;
import com.teachersession.mappers.CourseSessionMapper;
import com.teachersession.repositories.CourseSessionRepository;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.EnrollmentStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;
    private final UserRepository userRepository;
    private final CourseSessionMapper courseSessionMapper;
    private final EnrollmentService enrollmentService;

    @Transactional
    public CourseSessionDto createSession(CourseSessionDto dto) {
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new CourseSessionException(CourseSessionErrorCode.TEACHER_NOT_FOUND));
                
        if (teacher.getRole() != Role.TEACHER) {
            throw new CourseSessionException(CourseSessionErrorCode.ONLY_TEACHERS_CAN_CREATE_SESSIONS);
        }
        
        if (dto.getSessionType() == CourseSessionType.ONLINE && (dto.getMeetingLink() == null || dto.getMeetingLink().isEmpty())) {
            throw new CourseSessionException(CourseSessionErrorCode.ONLINE_SESSION_REQUIRES_MEETING_LINK);
        }
        
        if (dto.getSessionType() == CourseSessionType.OFFLINE && (dto.getLocation() == null || dto.getLocation().isEmpty())) {
            throw new CourseSessionException(CourseSessionErrorCode.OFFLINE_SESSION_REQUIRES_LOCATION);
        }

        CourseSession courseSession = CourseSession.builder()
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
                .status(CourseSessionStatus.OPEN)
                .build();

        return courseSessionMapper.toDto(courseSessionRepository.save(courseSession));
    }

    public List<CourseSessionDto> getAllSessions() {
        return courseSessionRepository.findAll().stream()
                .map(courseSessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseSessionDto> searchSessions(String search, CourseSessionType type) {
        String finalSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return courseSessionRepository.searchSessions(finalSearch, type).stream()
                .map(courseSessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public CourseSessionDto getSessionById(Long id) {
        return courseSessionRepository.findById(id)
                .map(courseSessionMapper::toDto)
                .orElseThrow(() -> new CourseSessionException(CourseSessionErrorCode.SESSION_NOT_FOUND));
    }
    
    public List<CourseSessionDto> getSessionsByTeacher(Long teacherId) {
        return courseSessionRepository.findByTeacherId(teacherId).stream()
                .map(courseSessionMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CourseSessionDto cancelSession(Long sessionId, Long teacherId) {
        CourseSession courseSession = courseSessionRepository.findById(sessionId)
                .orElseThrow(() -> new CourseSessionException(CourseSessionErrorCode.SESSION_NOT_FOUND));
                
        if (!courseSession.getTeacher().getId().equals(teacherId)) {
            throw new CourseSessionException(CourseSessionErrorCode.NOT_AUTHORIZED_TO_CANCEL);
        }
        
        if (courseSession.getStartDate().isBefore(LocalDateTime.now())) {
            throw new CourseSessionException(CourseSessionErrorCode.CANNOT_CANCEL_STARTED_SESSION);
        }
        
        courseSession.setStatus(CourseSessionStatus.CANCELLED);
        return courseSessionMapper.toDto(courseSessionRepository.save(courseSession));
    }

    @Transactional
    public CourseSessionDto updateSession(CourseSessionDto dto, Long teacherId) {
        CourseSession courseSession = courseSessionRepository.findById(dto.getId())
                .orElseThrow(() -> new CourseSessionException(CourseSessionErrorCode.SESSION_NOT_FOUND));

        if (!courseSession.getTeacher().getId().equals(teacherId)) {
            throw new CourseSessionException(CourseSessionErrorCode.NOT_AUTHORIZED_TO_CANCEL); // Assuming NOT_AUTHORIZED can be used here too
        }

        if (courseSession.getStartDate().isBefore(LocalDateTime.now())) {
            throw new CourseSessionException(CourseSessionErrorCode.CANNOT_UPDATE_STARTED_SESSION);
        }

        // We only allow updating some fields if it hasn't been cancelled
        if (courseSession.getStatus() == CourseSessionStatus.CANCELLED) {
             throw new CourseSessionException(CourseSessionErrorCode.SESSION_NOT_FOUND); // Quick hack, or a specific exception
        }

        courseSession.setTitle(dto.getTitle());
        courseSession.setDescription(dto.getDescription());
        courseSession.setSubject(dto.getSubject());
        courseSession.setPrice(dto.getPrice());
        courseSession.setMaxStudents(dto.getMaxStudents());
        courseSession.setSessionType(dto.getSessionType());
        courseSession.setLocation(dto.getLocation());
        courseSession.setMeetingLink(dto.getMeetingLink());
        courseSession.setStartDate(dto.getStartDate());
        courseSession.setDurationMinutes(dto.getDurationMinutes());
        
        return courseSessionMapper.toDto(courseSessionRepository.save(courseSession));
    }

    public void sessionFilter(Map<String, Object> params) {
        String search = (String) params.get("search");
        String type = (String) params.get("type");
        
        Object enrolledObj = params.get("enrolledOnly");
        boolean enrolledOnly = enrolledObj != null && Boolean.parseBoolean(enrolledObj.toString());
        
        Model model = (Model) params.get("model");
        HttpSession httpSession = (HttpSession) params.get("httpSession");

        CourseSessionType sessionType = null;

        if (type != null && !type.trim().isEmpty()) {
            try {
                sessionType = CourseSessionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid type
            }
        }
        
        List<CourseSessionDto> courseSessionDtos = searchSessions(search, sessionType);
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
            
            if (userDto.getRole() == Role.STUDENT) {
                List<Long> enrolledSessionIds = enrollmentService.getStudentEnrollments(userDto.getId()).stream()
                        .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                        .map(EnrollmentDto::getSessionId)
                        .collect(Collectors.toList());
                model.addAttribute("enrolledSessionIds", enrolledSessionIds);
                
                if (enrolledOnly) {
                    courseSessionDtos = courseSessionDtos.stream()
                        .filter(s -> enrolledSessionIds.contains(s.getId()))
                        .collect(Collectors.toList());
                }
            } else if (userDto.getRole() == Role.TEACHER) {
                List<Long> teacherSessionIds = getSessionsByTeacher(userDto.getId()).stream()
                        .map(CourseSessionDto::getId)
                        .collect(Collectors.toList());
                model.addAttribute("enrolledSessionIds", teacherSessionIds);
                
                if (enrolledOnly) {
                    courseSessionDtos = courseSessionDtos.stream()
                        .filter(s -> teacherSessionIds.contains(s.getId()))
                        .collect(Collectors.toList());
                }
            }
        }
        
        model.addAttribute("courseSessions", courseSessionDtos);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentType", type);
        model.addAttribute("enrolledOnly", enrolledOnly);
    }
}
