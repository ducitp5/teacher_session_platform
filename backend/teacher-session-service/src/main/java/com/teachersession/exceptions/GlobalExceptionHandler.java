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
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final BugLogRepository bugLogRepository;
    private final UserRepository userRepository;

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public String handleNotFoundError(Exception ex, Model model) {
        // Just return a standard 404 layout or redirect without logging it as a database bug.
        // Chrome DevTools constantly polls for standard map files which trigger this.
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, HttpSession session, Model model) {

        log.error("Unhandled Exception caught: ", ex);

        UserDto userDto = (UserDto) session.getAttribute("userDto");
        User user = null;

        boolean dbError = isDatabaseError(ex);
        String bugRefId = "N/A";
        String loggingErrorMessage = null;
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        if (!dbError) {
            try {
                if (userDto != null) {
                    user = userRepository.findById(userDto.getId()).orElse(null);
                }

                BugLog bugLog = BugLog.builder()
                        .user(user)
                        .message(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName())
                        .stackTrace(stackTrace)
                        .requestUrl(request.getRequestURL() != null ? request.getRequestURL().toString() : null)
                        .requestMethod(request.getMethod())
                        .build();

                bugLog = bugLogRepository.save(bugLog);
                bugRefId = String.valueOf(bugLog.getId());
            } catch (Exception loggingEx) {
                log.error("Failed to log bug to database. The database is likely down.", loggingEx);
                dbError = true;
                loggingErrorMessage = loggingEx.getMessage() != null ? loggingEx.getMessage() : loggingEx.getClass().getName();
            }
        }

        model.addAttribute("bugRefId", bugRefId);
        model.addAttribute("dbError", dbError);
        model.addAttribute("userDto", userDto);
        model.addAttribute("exceptionMessage", ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName());
        model.addAttribute("exceptionTrace", stackTrace);
        model.addAttribute("loggingErrorMessage", loggingErrorMessage);

        return "error/500";
    }

    private boolean isDatabaseError(Exception ex) {
        Throwable cause = ex;
        while (cause != null) {
            if (cause instanceof SQLException || cause instanceof DataAccessException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
