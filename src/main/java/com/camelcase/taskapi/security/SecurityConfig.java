package com.camelcase.taskapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = { 
                    "/api/auth/login", 
                    "/", "/*.html", 
                    "/favicon.ico", 
                    "/static/**", 
                    "/public/**", 
                    "/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/graphiql",  
                    "/graphql/schema" };

    @Value("${security.admin.username}")
    private String adminUsername;

    @Value("${security.admin.password}")
    private String adminPassword;

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())  // Disable CSRF for API requests
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/api/auth/login", "/", "/*.html", "/favicon.ico", "/static/**", "/public/**", "/swagger-ui/**", "/v3/api-docs/**", "/graphiql", "/vendor/**", "/graphql/schema", "/playground").permitAll()  // "/graphql",  ✅ Allow static resources
    //             .anyRequest().authenticated())  // Protect all other endpoints
    //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Enforce stateless JWT authentication
    //         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            

    //     return http.build();

    //     // http
    //     //     .csrf(csrf -> csrf.disable())  // Disable CSRF protection
    //     //     .authorizeHttpRequests(auth -> auth
    //     //         .anyRequest().permitAll()  // ✅ Allow all requests without authentication
    //     //     )
    //     //     .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //     // return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for API requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITE_LIST_URL).permitAll()  
                //.requestMatchers("/api/auth/login", "/", "/*.html", "/favicon.ico", "/static/**", "/public/**", "/swagger-ui/**", "/v3/api-docs/**", "/graphiql", "/vendor/**", "/graphql/schema").permitAll()  // "/graphql",  ✅ Allow static resources
                .requestMatchers("/graphql").authenticated()  // ✅ Require authentication for GraphQL API itself
                .anyRequest().authenticated())  
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/graphql")
    //                     .allowedOrigins("http://localhost:8080")
    //                     .allowedMethods("GET", "POST", "OPTIONS")
    //                     .allowedHeaders("*")
    //                     .allowCredentials(true);
    //         }
    //     };
    // }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (adminUsername.equals(username)) {
                return User.withUsername(adminUsername)
                        .password(passwordEncoder().encode(adminPassword))
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

}
