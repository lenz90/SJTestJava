package com.example.userapi.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final JwtUtil jwtUtil;

    public JwtProvider(
            @Value("${jwt.secret:default-very-strong-secret-0123456789}") String secret,
            @Value("${jwt.expiration:3600}") long expirationSeconds) {
        this.jwtUtil = new JwtUtil(secret, expirationSeconds);
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}