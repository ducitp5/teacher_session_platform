package com.teachersession.exceptions;

import com.teachersession.dto.UserDto;
import com.teachersession.entities.BugLog;
import com.teachersession.entities.User;
import com.teachersession.repositories.BugLogRepository;
import com.teachersession.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final BugLogRepository bugLogRepository;
    private final UserRepository userRepository;

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, HttpSession session, Model model) {

        log.error("Unhandled Exception caught: ", ex);

        User user = null;
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        if (userDto != null) {
            user = userRepository.findById(userDto.getId()).orElse(null);
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        BugLog bugLog = BugLog.builder()
                .user(user)
                .message(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName())
                .stackTrace(stackTrace)
                .requestUrl(request.getRequestURL() != null ? request.getRequestURL().toString() : null)
                .requestMethod(request.getMethod())
                .build();

        bugLog = bugLogRepository.save(bugLog);

        model.addAttribute("bugRefId", bugLog.getId());
        model.addAttribute("userDto", userDto);

        return "error/500";
    }
}
