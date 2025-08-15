package com.schoolapp.student_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // allow all paths
                .allowedOrigins("*") // allow all origins during development
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allow these methods
                .allowedHeaders("*"); // allow any headers
    }
}
