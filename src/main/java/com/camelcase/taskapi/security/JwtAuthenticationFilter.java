package com.camelcase.taskapi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// Note: using jakarta.servlet.Filter interface instead of standard OncePerRequestFilter due to 
// compatibility issues with Spring GraphQL
// https://github.com/spring-projects/spring-graphql/issues/594

public class JwtAuthenticationFilter implements jakarta.servlet.Filter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/auth/login",
        ".html",
        "favicon.ico",
        "/v3/api-docs",
        "/public",
        "/graphiql"
    };

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (isPublicEndpoint(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        //  Handle missing token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "No authentication token provided.");
            return;
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        } catch (ExpiredJwtException e) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Your session has expired. Please log in again.");
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication token.");
        } catch (Exception e) {
            handleError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestPath) {
        if ("/".equals(requestPath)) {  // Allow root path
            return true;
        }
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (requestPath.endsWith(endpoint)) {
                return true;
            }
        }
        return false;
    }

    private void handleError(HttpServletResponse response, int status, String message) throws IOException {
        logger.warn("Authentication error: {}", message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message));
    }
    
}
