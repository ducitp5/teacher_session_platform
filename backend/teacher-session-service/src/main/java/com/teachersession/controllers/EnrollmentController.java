package com.teachersession.controllers;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.entities.enums.Role;
import com.teachersession.services.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model, HttpSession session) {
        Long studentId = checkStudentAuth(session);
        if (studentId == null) return "redirect:/login";
        
        List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments(studentId);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("userFirstName", session.getAttribute("userFirstName"));
        return "student/dashboard";
    }

    @PostMapping("/sessions/{id}/enroll")
    public String enrollStudent(@PathVariable Long id, HttpSession session) {
        Long studentId = checkStudentAuth(session);
        if (studentId == null) return "redirect:/login";
        
        try {
            enrollmentService.enrollStudent(id, studentId);
            return "redirect:/student/dashboard?success=enrolled";
        } catch (Exception e) {
            return "redirect:/sessions/" + id + "?error=" + e.getMessage();
        }
    }

    @PostMapping("/student/enrollments/{enrollmentId}/cancel")
    public String cancelEnrollment(@PathVariable Long enrollmentId, HttpSession session) {
        Long studentId = checkStudentAuth(session);
        if (studentId == null) return "redirect:/login";
        
        try {
            enrollmentService.cancelEnrollment(enrollmentId, studentId);
            return "redirect:/student/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/student/dashboard?error=" + e.getMessage();
        }
    }

    private Long checkStudentAuth(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String roleStr = (String) session.getAttribute("userRole");
        if (userId == null || roleStr == null) return null;
        
        Role role = Role.valueOf(roleStr);
        if (role != Role.STUDENT) return null;
        return userId;
    }
}
