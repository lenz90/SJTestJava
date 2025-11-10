package com.example.userapi.shared.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {

    private final Key signingKey;
    private final long expirationSeconds;

    public JwtUtil(String secret, long expirationSeconds) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Date exp = Date.from(now.plusSeconds(expirationSeconds));
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(exp)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}