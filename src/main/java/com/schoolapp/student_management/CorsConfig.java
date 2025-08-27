package com.schoolapp.student_management;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins(
                        "http://localhost:3000",   // Local React (old)
                        "http://localhost:3001",   // Local React (new, your case)
                        "https://student-management-frontend-vq9m.onrender.com" // Render frontend
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // Needed if you use cookies/auth
    }
}
