package com.teachersession.config.interceptor;

import com.teachersession.dto.UserDto;
import com.teachersession.entities.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class StudentAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserDto userDto = (UserDto) session.getAttribute("userDto");
            if (userDto != null && userDto.getRole() == Role.STUDENT) {
                return true; // Proceed to Controller
            }
        }
        
        response.sendRedirect("/login");
        return false; // Abort Controller execution
    }
}
