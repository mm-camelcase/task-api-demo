package com.camelcase.taskapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final Key key;

    // Load the secret key from the application properties
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .claim("sub", username)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 1 hour expiry
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) key)
            .build().parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
