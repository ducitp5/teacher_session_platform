package com.teachersession.controllers;

import com.teachersession.dto.EnrollmentDto;
import com.teachersession.dto.UserDto;
import com.teachersession.services.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.teachersession.services.StudentService;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController extends UserController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/dashboard")
    public String studentDashboard(Model model, HttpSession httpSession) {
        Long studentId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments(studentId);
        model.addAttribute("enrollments", enrollments);
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
        }
        return "student/dashboard";
    }

    @PostMapping("/enrollments/{enrollmentId}/cancel")
    public String cancelEnrollment(@PathVariable Long enrollmentId, HttpSession httpSession) {
        Long studentId = ((UserDto) httpSession.getAttribute("userDto")).getId();
        
        try {
            enrollmentService.cancelEnrollment(enrollmentId, studentId);
            return "redirect:/student/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/student/dashboard?error=" + e.getMessage();
        }
    }
}
