package com.example.userapi.application.ports.out;

import com.example.userapi.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findAll();
}