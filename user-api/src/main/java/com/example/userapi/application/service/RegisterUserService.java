package com.example.userapi.application.service;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.ports.in.RegisterUserUseCase;
import com.example.userapi.application.ports.out.UserRepositoryPort;
import com.example.userapi.application.exception.UserAlreadyExistsException;
import com.example.userapi.domain.model.Phone;
import com.example.userapi.domain.model.User;
import com.example.userapi.shared.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public RegisterUserService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    private String nowIso() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now().atOffset(ZoneOffset.UTC));
    }

    @Override
    public UserResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Correo ya registrado");
        }

        UUID id = UUID.randomUUID();
        String created = nowIso();
        String modified = created;
        String lastLogin = created;

        String token = jwtProvider.generateToken(request.getEmail());

        User user = new User(
                id,
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                null,
                token,
                created,
                modified,
                lastLogin,
                true
        );

        if (request.getPhones() != null) {
            List<Phone> phones = request.getPhones().stream()
                    .map(p -> {
                        Phone ph = new Phone();
                        ph.setNumber(p.getNumber());
                        ph.setCitycode(p.getCitycode());
                        ph.setCountrycode(p.getCountrycode());
                        ph.setUser(user);
                        return ph;
                    }).collect(Collectors.toList());
            user.setPhones(phones);
        }

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getCreated(), saved.getModified(), saved.getLastLogin(), saved.getToken(), saved.isActive());
    }

    @Override
    public UserResponse getUserById(String id) {
        UUID uuid = UUID.fromString(id);
        return userRepository.findById(uuid)
                .map(u -> new UserResponse(u.getId(), u.getCreated(), u.getModified(), u.getLastLogin(), u.getToken(), u.isActive()))
                .orElse(null);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getCreated(), u.getModified(), u.getLastLogin(), u.getToken(), u.isActive()))
                .collect(Collectors.toList());
    }
}