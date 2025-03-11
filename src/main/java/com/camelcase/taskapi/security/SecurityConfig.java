package com.camelcase.taskapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for API requests
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("index.html", "/api/auth/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/auth/login", "/", "/*.html", "/favicon.ico", "/static/**", "/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()  // ✅ Allow static resources
                .anyRequest().authenticated())  // Protect all other endpoints
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Enforce stateless JWT authentication
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            //.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)); // Handle authentication errors 

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("admin".equals(username)) {
                return User.withUsername("admin")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .build();
            } else {
                throw new RuntimeException("User not found");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // @Bean
    // public OpenAPI customOpenAPI() {
    //     return new OpenAPI()
    //         .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
    //         .components(new Components()
    //             .addSecuritySchemes("bearerAuth", new SecurityScheme()
    //                 .type(SecurityScheme.Type.HTTP)
    //                 .scheme("bearer")
    //                 .bearerFormat("JWT")));
    // }
}
