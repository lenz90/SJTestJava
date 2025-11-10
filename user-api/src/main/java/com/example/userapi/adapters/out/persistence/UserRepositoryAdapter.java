package com.example.userapi.adapters.out.persistence;

import com.example.userapi.application.ports.out.UserRepositoryPort;
import com.example.userapi.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springRepo;

    public UserRepositoryAdapter(SpringDataUserRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public User save(User user) {
        return springRepo.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springRepo.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springRepo.findById(id);
    }

    @Override
    public List<User> findAll() {
        return springRepo.findAll();
    }
}