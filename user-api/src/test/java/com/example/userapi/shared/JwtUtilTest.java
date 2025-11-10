package com.example.userapi.shared;

import com.example.userapi.shared.security.JwtUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateAndParseToken_shouldReturnUsernameAndExpiration() {
        String secret = "my-very-strong-secret-that-is-long-enough-0123456789";
        long ttl = 3600L; // 1 hour

        JwtUtil jwtUtil = new JwtUtil(secret, ttl);

        String token = jwtUtil.generateToken("juan@example.com");
        assertNotNull(token);

        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("juan@example.com", username);

        Date exp = jwtUtil.getExpirationDateFromToken(token);
        assertNotNull(exp);
        assertTrue(exp.getTime() > System.currentTimeMillis());
    }
}