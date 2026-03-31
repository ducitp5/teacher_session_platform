package com.teachersession.controllers;

import com.teachersession.dto.SessionDto;
import com.teachersession.dto.EnrollmentDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.EnrollmentStatus;
import com.teachersession.entities.enums.Role;
import com.teachersession.services.SessionService;
import com.teachersession.services.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;
import com.teachersession.entities.enums.SessionType;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/")
    public String home(@RequestParam Map<String, String> requestParams, Model model, HttpSession httpSession) {

        Map<String, Object> params = new HashMap<>(requestParams);
        params.put("model", model);
        params.put("httpSession", httpSession);

        sessionFilter(params);

        return "sessions/index";
    }

    @GetMapping("/sessions/search")
    public String searchSessionsAjax(@RequestParam Map<String, String> requestParams, Model model, HttpSession httpSession) {

        Map<String, Object> params = new HashMap<>(requestParams);
        params.put("model", model);
        params.put("httpSession", httpSession);

        sessionFilter(params);

        return "sessions/include/session-list";
    }

    @GetMapping("/sessions/{sessionId}")
    public String sessionDetails(@PathVariable Long sessionId, Model model, HttpSession httpSession) {

        SessionDto sessionDto = sessionService.getSessionById(sessionId);
        model.addAttribute("courseSession", sessionDto);
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        
        return "sessions/detail";
    }

    // Teacher specific routes
    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(Model model, HttpSession httpSession) {

        Long teacherId = checkTeacherAuth(httpSession);
        if (teacherId == null) return "redirect:/login";
        
        List<SessionDto> sessions = sessionService.getSessionsByTeacher(teacherId);
        model.addAttribute("courSessions", sessions);
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        return "teacher/dashboard";
    }

    @GetMapping("/teacher/sessions/create")
    public String showCreateSessionForm(Model model, HttpSession httpSession) {
        if (checkTeacherAuth(httpSession) == null) return "redirect:/login";
        model.addAttribute("sessionDto", new SessionDto());
        return "teacher/create_session";
    }

    @PostMapping("/teacher/sessions/create")
    public String createSession(@ModelAttribute SessionDto sessionDto, HttpSession httpSession, Model model) {
        Long teacherId = checkTeacherAuth(httpSession);
        if (teacherId == null) return "redirect:/login";
        
        try {
            sessionDto.setTeacherId(teacherId);
            sessionService.createSession(sessionDto);
            return "redirect:/teacher/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "teacher/create_session";
        }
    }
    
    @PostMapping("/teacher/sessions/{id}/cancel")
    public String cancelSession(@PathVariable Long id, HttpSession httpSession) {
        Long teacherId = checkTeacherAuth(httpSession);
        if (teacherId == null) return "redirect:/login";
        
        try {
            sessionService.cancelSession(id, teacherId);
            return "redirect:/teacher/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/teacher/dashboard?error=" + e.getMessage();
        }
    }

    private Long checkTeacherAuth(HttpSession httpSession) {
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto == null || userDto.getRole() != Role.TEACHER) return null;
        return userDto.getId();
    }

    private void sessionFilter(Map<String, Object> params) {

        String search = (String) params.get("search");
        String type = (String) params.get("type");
        
        Object enrolledObj = params.get("enrolledOnly");
        boolean enrolledOnly = enrolledObj != null && Boolean.parseBoolean(enrolledObj.toString());
        
        Model model = (Model) params.get("model");
        HttpSession httpSession = (HttpSession) params.get("httpSession");

        SessionType sessionType = null;

        if (type != null && !type.trim().isEmpty()) {
            try {
                sessionType = SessionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid type
            }
        }
        
        List<SessionDto> sessionDtos = sessionService.searchSessions(search, sessionType);
        
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
                    sessionDtos = sessionDtos.stream()
                        .filter(s -> enrolledSessionIds.contains(s.getId()))
                        .collect(Collectors.toList());
                }
            } else if (userDto.getRole() == Role.TEACHER) {
                List<Long> teacherSessionIds = sessionService.getSessionsByTeacher(userDto.getId()).stream()
                        .map(SessionDto::getId)
                        .collect(Collectors.toList());
                model.addAttribute("enrolledSessionIds", teacherSessionIds);
                
                if (enrolledOnly) {
                    sessionDtos = sessionDtos.stream()
                        .filter(s -> teacherSessionIds.contains(s.getId()))
                        .collect(Collectors.toList());
                }
            }
        }
        
        model.addAttribute("sessionDtos", sessionDtos);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentType", type);
        model.addAttribute("enrolledOnly", enrolledOnly);
    }
}
