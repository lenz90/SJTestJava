package com.example.userapi.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    private String token;

    // NOTE: Using String for created/modified/lastLogin to match existing compiled expectations
    private String created;
    private String modified;
    private String lastLogin;
    private boolean isActive;

    public User() {
        // no-args
    }

    // constructor con la firma que mostró el error
    public User(UUID id, String name, String email, String password, List<Phone> phones,
                String token, String created, String modified, String lastLogin, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones != null ? phones : new ArrayList<>();
        this.token = token;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    // getters / setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Phone> getPhones() { return phones; }
    public void setPhones(List<Phone> phones) {
        this.phones.clear();
        if (phones != null) this.phones.addAll(phones);
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    public String getModified() { return modified; }
    public void setModified(String modified) { this.modified = modified; }

    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}