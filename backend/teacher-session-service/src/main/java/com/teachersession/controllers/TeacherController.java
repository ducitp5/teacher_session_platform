package com.teachersession.controllers;

import com.teachersession.dto.CourseSessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.dto.EnrollmentDto;
import com.teachersession.services.TeacherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController extends UserController {

    private final TeacherService teacherService;

    @GetMapping("/dashboard")
    public String teacherDashboard(Model model, HttpSession httpSession) {

        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        List<CourseSessionDto> courseSessions = teacherService.getTeacherSessions(teacherId);
        model.addAttribute("courseSessions", courseSessions);
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        return "teacher/dashboard";
    }

    @GetMapping("/sessions/create")
    public String showCreateSessionForm(Model model, HttpSession httpSession) {
        model.addAttribute("courseSessionDto", new CourseSessionDto());
        return "teacher/create_session";
    }

    @PostMapping("/sessions/create")
    public String createSession(@ModelAttribute CourseSessionDto courseSessionDto, HttpSession httpSession, Model model) {
        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        try {
            teacherService.createTeacherSession(courseSessionDto, teacherId);
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
            teacherService.cancelTeacherSession(id, teacherId);
            return "redirect:/teacher/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/teacher/dashboard?error=" + e.getMessage();
        }
    }

    @GetMapping("/sessions/{id}")
    public String teacherSessionDetails(@PathVariable Long id, Model model, HttpSession httpSession) {
        CourseSessionDto courseSessionDto = teacherService.getSessionById(id);
        
        List<EnrollmentDto> enrollments = teacherService.getSessionEnrollments(id);
        model.addAttribute("courseSession", courseSessionDto);
        model.addAttribute("enrollments", enrollments);
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        
        return "teacher/session_detail";
    }

    @GetMapping("/sessions/{id}/edit")
    public String showEditSessionForm(@PathVariable Long id, Model model, HttpSession httpSession) {
        CourseSessionDto courseSessionDto = teacherService.getSessionById(id);
        model.addAttribute("courseSessionDto", courseSessionDto);
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        
        return "teacher/edit_session";
    }

    @PostMapping("/sessions/{id}/edit")
    public String updateSession(@ModelAttribute CourseSessionDto courseSessionDto, HttpSession httpSession, Model model) {
        Long teacherId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        try {
            teacherService.updateTeacherSession(courseSessionDto, teacherId);
            return "redirect:/teacher/sessions/" + courseSessionDto.getId() + "?success=updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("courseSessionDto", courseSessionDto);
            return "teacher/edit_session";
        }
    }
}
