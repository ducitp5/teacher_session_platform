package com.teachersession.controllers;

import com.teachersession.dto.SessionDto;
import com.teachersession.entities.enums.Role;
import com.teachersession.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<SessionDto> sessions = sessionService.getAllSessions();
        model.addAttribute("sessions", sessions);
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("userId", userId);
            model.addAttribute("userRole", session.getAttribute("userRole"));
            model.addAttribute("userFirstName", session.getAttribute("userFirstName"));
        }
        
        return "sessions/index";
    }

    @GetMapping("/sessions/{id}")
    public String sessionDetails(@PathVariable Long id, Model model, HttpSession session) {
        SessionDto sessionDto = sessionService.getSessionById(id);
        model.addAttribute("courseSession", sessionDto);
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("userId", userId);
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        
        return "sessions/detail";
    }

    // Teacher specific routes
    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(Model model, HttpSession session) {
        Long teacherId = checkTeacherAuth(session);
        if (teacherId == null) return "redirect:/login";
        
        List<SessionDto> sessions = sessionService.getSessionsByTeacher(teacherId);
        model.addAttribute("courSessions", sessions);
        model.addAttribute("userFirstName", session.getAttribute("userFirstName"));
        return "teacher/dashboard";
    }

    @GetMapping("/teacher/sessions/create")
    public String showCreateSessionForm(Model model, HttpSession session) {
        if (checkTeacherAuth(session) == null) return "redirect:/login";
        model.addAttribute("sessionDto", new SessionDto());
        return "teacher/create_session";
    }

    @PostMapping("/teacher/sessions/create")
    public String createSession(@ModelAttribute SessionDto sessionDto, HttpSession session, Model model) {
        Long teacherId = checkTeacherAuth(session);
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
    public String cancelSession(@PathVariable Long id, HttpSession session) {
        Long teacherId = checkTeacherAuth(session);
        if (teacherId == null) return "redirect:/login";
        
        try {
            sessionService.cancelSession(id, teacherId);
            return "redirect:/teacher/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/teacher/dashboard?error=" + e.getMessage();
        }
    }

    private Long checkTeacherAuth(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String roleStr = (String) session.getAttribute("userRole");
        if (userId == null || roleStr == null) return null;
        
        Role role = Role.valueOf(roleStr);
        if (role != Role.TEACHER) return null;
        return userId;
    }
}
