package com.example.userapi.shared.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {
    private String emailRegex;
    private String passwordRegex;

    public String getEmailRegex() { return emailRegex; }
    public void setEmailRegex(String emailRegex) { this.emailRegex = emailRegex; }
    public String getPasswordRegex() { return passwordRegex; }
    public void setPasswordRegex(String passwordRegex) { this.passwordRegex = passwordRegex; }

    @PostConstruct
    public void init() {
        // opcional: validar que existan las regex
    }
}