package com.example.userapi.application;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.exception.UserAlreadyExistsException;
import com.example.userapi.application.service.RegisterUserService;
import com.example.userapi.application.ports.out.UserRepositoryPort;
import com.example.userapi.domain.model.User;
import com.example.userapi.shared.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider; // agregado

    @InjectMocks
    private RegisterUserService registerUserService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserRequest();
        validRequest.setName("Juan Perez");
        validRequest.setEmail("juan@example.com");
        validRequest.setPassword("Secreto123");
    }

    @Test
    void register_success_shouldReturnUserResponseAndSave() {
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("hashedPassword");
        when(jwtProvider.generateToken(validRequest.getEmail())).thenReturn("token-123"); // stub

        UUID generatedId = UUID.randomUUID();
        User saved = new User(
                generatedId,
                validRequest.getName(),
                validRequest.getEmail(),
                "hashedPassword",
                null,
                "token-123",
                "2025-01-01T00:00:00Z",
                "2025-01-01T00:00:00Z",
                "2025-01-01T00:00:00Z",
                true
        );
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponse resp = registerUserService.register(validRequest);

        assertNotNull(resp);
        assertEquals(generatedId, resp.getId());
        assertNotNull(resp.getToken());
        verify(userRepository).existsByEmail(validRequest.getEmail());
        verify(passwordEncoder).encode(validRequest.getPassword());
        verify(jwtProvider).generateToken(validRequest.getEmail());
        verify(userRepository).save(userCaptor.capture());

        User captured = userCaptor.getValue();
        assertEquals(validRequest.getName(), captured.getName());
        assertEquals(validRequest.getEmail(), captured.getEmail());
        assertEquals("hashedPassword", captured.getPassword());
    }

    @Test
    void register_duplicateEmail_shouldThrowException() {
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () ->
                registerUserService.register(validRequest));

        assertEquals("Correo ya registrado", ex.getMessage());
        verify(userRepository).existsByEmail(validRequest.getEmail());
        verify(userRepository, never()).save(any());
    }
}