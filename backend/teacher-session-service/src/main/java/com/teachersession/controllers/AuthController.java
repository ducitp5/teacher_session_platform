package com.teachersession.controllers;

import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.Role;
import com.teachersession.exceptions.AuthException;
import com.teachersession.services.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            UserDto user = authService.login(email, password);

            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("userFirstName", user.getFirstName());

            return (user.getRole() == Role.TEACHER)
                    ? "redirect:/teacher/dashboard"
                    : "redirect:/";

        } catch (AuthException ex) {
            model.addAttribute("error", ex.getErrorCode().getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String role,
                           Model model) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            authService.register(email, password, firstName, lastName, userRole);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
