package com.example.userapi.adapters.in.web;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.exception.UserAlreadyExistsException;
import com.example.userapi.application.ports.in.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse resp = registerUserUseCase.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        UserResponse resp = registerUserUseCase.getUserById(id);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> all = registerUserUseCase.getAllUsers();
        return ResponseEntity.ok(all);
    }
}