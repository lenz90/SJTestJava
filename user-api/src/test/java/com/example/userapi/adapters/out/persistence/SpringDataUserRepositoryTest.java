package com.example.userapi.adapters.out.persistence;

import com.example.userapi.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // usar H2 en memoria
@EntityScan("com.example.userapi.domain.model") // asegurar scan de entidades
class SpringDataUserRepositoryTest {

    @Autowired
    private SpringDataUserRepository repo;

    @Test
    void saveAndFindByEmailAndExistsByEmail() {
        UUID id = UUID.randomUUID();
        User u = new User();
        u.setId(id);
        u.setName("Prueba");
        u.setEmail("test@example.com");
        u.setPassword("pwd");
        u.setToken("t");
        u.setCreated("2025-01-01T00:00:00Z");
        u.setModified("2025-01-01T00:00:00Z");
        u.setLastLogin("2025-01-01T00:00:00Z");
        u.setActive(true);

        User saved = repo.save(u);

        assertThat(saved).isNotNull();
        assertThat(repo.existsByEmail("test@example.com")).isTrue();
        assertThat(repo.findById(id)).isPresent();
        assertThat(repo.findByEmail("test@example.com")).isPresent();
    }
}