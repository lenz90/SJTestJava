package com.example.userapi.application.dto;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    // usar String para fechas (coincide con User)
    private String created;
    private String modified;
    private String lastLogin;
    private String token;
    private boolean isActive;

    public UserResponse(UUID id, String created, String modified, String lastLogin, String token, boolean isActive) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

    public UUID getId() { return id; }
    public String getCreated() { return created; }
    public String getModified() { return modified; }
    public String getLastLogin() { return lastLogin; }
    public String getToken() { return token; }
    public boolean isIsActive() { return isActive; }
}