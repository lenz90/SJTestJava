package com.example.userapi.application.ports.in;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;

import java.util.List;

public interface RegisterUserUseCase {
    UserResponse register(UserRequest request);
    UserResponse getUserById(String id);
    List<UserResponse> getAllUsers();
}