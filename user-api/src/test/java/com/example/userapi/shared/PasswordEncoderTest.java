package com.example.userapi.shared;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    void bcrypt_shouldHashAndMatch() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "Secreto123!";
        String hashed = encoder.encode(raw);

        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$2")); // indica formato bcrypt
        assertTrue(encoder.matches(raw, hashed));
        assertFalse(encoder.matches("wrong", hashed));
    }
}