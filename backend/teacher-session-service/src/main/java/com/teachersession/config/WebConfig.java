package com.teachersession.config;

import com.teachersession.config.interceptor.StudentAuthInterceptor;
import com.teachersession.config.interceptor.TeacherAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StudentAuthInterceptor studentAuthInterceptor;
    private final TeacherAuthInterceptor teacherAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Registering Laravel-like middleware for paths
        registry.addInterceptor(studentAuthInterceptor)
                .addPathPatterns("/student/**");
                
        registry.addInterceptor(teacherAuthInterceptor)
                .addPathPatterns("/teacher/**");
    }
}
