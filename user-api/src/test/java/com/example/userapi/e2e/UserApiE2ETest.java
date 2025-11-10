package com.example.userapi.e2e;

import com.example.userapi.application.dto.UserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.shared.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "jwt.secret=my-very-strong-secret-that-is-long-enough-0123456789",
                "jwt.expiration=3600",
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        })
class UserApiE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerEndpoint_returnsUserWithValidJwt_and_getByIdWorks() {
        UserRequest req = new UserRequest();
        req.setName("E2E User");
        req.setEmail("e2e@example.com");
        req.setPassword("Secreto123!");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRequest> request = new HttpEntity<>(req, headers);
        ResponseEntity<UserResponse> resp = restTemplate.postForEntity("/users", request, UserResponse.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserResponse body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getToken()).isNotNull();

        // parse token with same secret used by the application (see properties above)
        JwtUtil parser = new JwtUtil("my-very-strong-secret-that-is-long-enough-0123456789", 3600L);
        String username = parser.getUsernameFromToken(body.getToken());
        assertThat(username).isEqualTo("e2e@example.com");

        Date exp = parser.getExpirationDateFromToken(body.getToken());
        assertThat(exp).isNotNull();
        assertThat(exp.getTime()).isGreaterThan(System.currentTimeMillis());

        // GET by id
        ResponseEntity<UserResponse> getResp = restTemplate.getForEntity("/users/" + body.getId(), UserResponse.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).isNotNull();
        assertThat(getResp.getBody().getId()).isEqualTo(body.getId());
    }
}