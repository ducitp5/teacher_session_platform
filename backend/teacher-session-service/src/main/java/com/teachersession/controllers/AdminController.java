package com.teachersession.controllers;

import com.teachersession.dto.CourseSessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.Role;
import com.teachersession.entities.enums.UserStatus;
import com.teachersession.services.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends UserController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession httpSession) {
        if (!isAdmin(httpSession)) return "redirect:/login";

        model.addAttribute("totalUsers", adminService.getTotalUsers());
        model.addAttribute("totalSessions", adminService.getTotalSessions());
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        model.addAttribute("userDto", userDto);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession httpSession) {
        if (!isAdmin(httpSession)) return "redirect:/login";

        List<UserDto> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        
        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        model.addAttribute("userDto", userDto);

        return "admin/users";
    }

    @PostMapping("/users/{id}/status")
    public String updateUserStatus(@PathVariable Long id, @RequestParam UserStatus status, HttpSession httpSession) {
        if (!isAdmin(httpSession)) return "redirect:/login";

        adminService.updateUserStatus(id, status);
        return "redirect:/admin/users?success=status_updated";
    }

    @GetMapping("/sessions")
    public String listSessions(Model model, HttpSession httpSession) {
        if (!isAdmin(httpSession)) return "redirect:/login";

        List<CourseSessionDto> courseSessions = adminService.getAllSessions();
        model.addAttribute("courseSessions", courseSessions);

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        model.addAttribute("userDto", userDto);

        return "admin/sessions";
    }

    @PostMapping("/sessions/{id}/cancel")
    public String cancelSession(@PathVariable Long id, HttpSession httpSession) {
        if (!isAdmin(httpSession)) return "redirect:/login";

        adminService.cancelSession(id);
        return "redirect:/admin/sessions?success=session_cancelled";
    }

    private boolean isAdmin(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("userDto");
        return user != null && user.getRole() == Role.ADMIN;
    }
}
