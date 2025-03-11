package com.camelcase.taskapi.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.camelcase.taskapi.dto.AuthRequest;
import com.camelcase.taskapi.security.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    //private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        //this.userDetailsService = userDetailsService;
    }

   

    //@PostMapping("/login")
    // @PostMapping(value = "/login", consumes = {"application/json", "application/x-www-form-urlencoded"})
    // public Map<String, String> login(
    //         @RequestParam(required = false) String username, 
    //         @RequestParam(required = false) String password, 
    //         @RequestBody(required = false) AuthRequest request
    //     ) {
        
    //     if (request != null) { // JSON payload handling
    //         username = request.getUsername();
    //         password = request.getPassword();
    //     }
        
    //     logger.info("Received TestRequest: {}", request);

    //     authenticationManager.authenticate(
    //         new UsernamePasswordAuthenticationToken(username, password)
    //     );

    //     String token = jwtUtil.generateToken(username);

    //     //return Map.of("token", token);

    //     return Map.of(
    //         "access_token", token,
    //         "token_type", "Bearer",
    //         "expires_in", String.valueOf(3600)  // Example: Token expiry in seconds
    //     );


    //     //return ResponseEntity.ok("Received: " + request);
    // }

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
public Map<String, String> loginFormData(
        @RequestParam String username,
        @RequestParam String password) {

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

@PostMapping(value = "/login", consumes = "application/json")
public Map<String, String> loginJson(@RequestBody AuthRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    String token = jwtUtil.generateToken(request.getUsername());

    return Map.of(
        "access_token", token,
        "token_type", "Bearer",
        "expires_in", "3600"
    );
}

}
