package com.camelcase.taskapi.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.camelcase.taskapi.dto.AuthRequest;
import com.camelcase.taskapi.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public Map<String, String> loginFormData(
            @RequestParam String username,
            @RequestParam String password) {
        return authenticateAndGenerateToken(username, password);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public Map<String, String> loginJson(@RequestBody AuthRequest request) {
        return authenticateAndGenerateToken(request.getUsername(), request.getPassword());
    }

    private Map<String, String> authenticateAndGenerateToken(String username, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
    
        String token = jwtUtil.generateToken(username);
    
        return Map.of(
            "access_token", token,
            "token_type", "Bearer",
            "expires_in", "3600"
        );
    }

}
