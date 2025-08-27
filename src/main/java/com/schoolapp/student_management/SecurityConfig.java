package com.schoolapp.student_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🔓 Allow requests without authentication (you can restrict later if needed)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // allow all endpoints for now
            )
            // ❌ Disable CSRF (important for API usage with frontend)
            .csrf(csrf -> csrf.disable())
            // 🌍 CORS enabled (will use WebConfig.java rules)
            .cors();

        return http.build();
    }
}
