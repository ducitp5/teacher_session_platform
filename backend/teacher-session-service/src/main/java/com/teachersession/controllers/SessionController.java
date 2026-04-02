package com.teachersession.controllers;

import com.teachersession.dto.SessionDto;
import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.Role;
import com.teachersession.services.SessionService;
import com.teachersession.services.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestParam;

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

        sessionService.sessionFilter(params);

        return "sessions/index";
    }

    @GetMapping("/sessions/search")
    public String searchSessionsAjax(@RequestParam Map<String, String> requestParams, Model model, HttpSession httpSession) {

        Map<String, Object> params = new HashMap<>(requestParams);
        params.put("model", model);
        params.put("httpSession", httpSession);

        sessionService.sessionFilter(params);

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

    @PostMapping("/sessions/{sessionId}/enroll")
    public String enrollStudent(@PathVariable Long sessionId, HttpSession httpSession) {

        UserDto userDto = (UserDto) httpSession.getAttribute("userDto");
        if (userDto == null || userDto.getRole() != Role.STUDENT) return "redirect:/login";
        
        Long studentId = userDto.getId();
        
        try {
            enrollmentService.enrollStudent(sessionId, studentId);
            return "redirect:/student/dashboard?success=enrolled";
        } catch (Exception e) {
            return "redirect:/sessions/" + sessionId + "?error=" + e.getMessage();
        }
    }
}
