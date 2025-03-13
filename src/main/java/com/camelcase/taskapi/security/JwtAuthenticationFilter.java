package com.camelcase.taskapi.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // ✅ Skip filter for public endpoints
        String requestPath = request.getServletPath();
        if (requestPath.equals("/api/auth/login") || requestPath.equals("/") || requestPath.endsWith(".html") || requestPath.endsWith("favicon.ico") || requestPath.startsWith("/swagger-ui") || requestPath.startsWith("/v3/api-docs") || requestPath.startsWith("/public") || requestPath.startsWith("/graphql") || requestPath.startsWith("/graphiql")|| requestPath.startsWith("/vendor")) {
            chain.doFilter(request, response);
            return;
        }

        //  Handle missing token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header for request: {}", request.getRequestURI());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"No authentication token provided.\"}");
            return;
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired for request {}: {}", request.getRequestURI(), e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Your session has expired. Please log in again.\"}");
            return;
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e) {
            // ✅ Handle corrupt tokens
            logger.warn("Invalid JWT token for request {}: {}", request.getRequestURI(), e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid authentication token.\"}");
            return;
        } catch (Exception e) {
            logger.error("Unexpected authentication error for request {}: {}", request.getRequestURI(), e.getMessage());

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access denied.\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
