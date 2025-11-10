package com.example.userapi.adapters.in.web;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.exception.UserAlreadyExistsException;
import com.example.userapi.application.ports.in.RegisterUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void register_success_shouldReturnCreated() throws Exception {
        UserRequest req = new UserRequest();
        req.setName("Juan");
        req.setEmail("juan@example.com");
        req.setPassword("Secreto123");

        UserResponse resp = new UserResponse(
                UUID.randomUUID(),
                "2025-01-01T00:00:00Z",
                "2025-01-01T00:00:00Z",
                "2025-01-01T00:00:00Z",
                "token-123",
                true
        );

        Mockito.when(registerUserUseCase.register(any(UserRequest.class))).thenReturn(resp);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.token", is("token-123")));
    }

    @Test
    void register_duplicate_shouldReturnConflict() throws Exception {
        UserRequest req = new UserRequest();
        req.setName("Juan");
        req.setEmail("juan@example.com");
        req.setPassword("Secreto123");

        Mockito.when(registerUserUseCase.register(any(UserRequest.class)))
                .thenThrow(new UserAlreadyExistsException("Correo ya registrado"));

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje", is("Correo ya registrado")));
    }

    @Test
    void register_validationFail_shouldReturnBadRequest() throws Exception {
        // invalid email and short password
        String body = """
                {
                  "name":"Juan",
                  "email":"not-an-email",
                  "password":"short"
                }
                """;

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_shouldReturnOk() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}