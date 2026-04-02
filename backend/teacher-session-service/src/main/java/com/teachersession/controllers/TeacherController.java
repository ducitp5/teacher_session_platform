package com.teachersession.controllers;

import com.teachersession.dto.SessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.services.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final SessionService sessionService;

    @GetMapping("/dashboard")
    public String teacherDashboard(Model model, HttpSession httpSession) {

        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        List<SessionDto> sessions = sessionService.getSessionsByTeacher(teacherId);
        model.addAttribute("courSessions", sessions);
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        return "teacher/dashboard";
    }

    @GetMapping("/sessions/create")
    public String showCreateSessionForm(Model model, HttpSession httpSession) {
        model.addAttribute("sessionDto", new SessionDto());
        return "teacher/create_session";
    }

    @PostMapping("/sessions/create")
    public String createSession(@ModelAttribute SessionDto sessionDto, HttpSession httpSession, Model model) {
        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        try {
            sessionDto.setTeacherId(teacherId);
            sessionService.createSession(sessionDto);
            return "redirect:/teacher/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "teacher/create_session";
        }
    }
    
    @PostMapping("/sessions/{id}/cancel")
    public String cancelSession(@PathVariable Long id, HttpSession httpSession) {
        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        try {
            sessionService.cancelSession(id, teacherId);
            return "redirect:/teacher/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/teacher/dashboard?error=" + e.getMessage();
        }
    }
}
