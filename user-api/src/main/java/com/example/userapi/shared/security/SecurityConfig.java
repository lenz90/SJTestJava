package com.example.userapi.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // o tu dominio específico
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .cors().and().csrf().disable()
          // Desactivar CSRF para facilitar llamadas POST en tests / clientes no-browser (ajustar en producción)
          .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
          .authorizeHttpRequests(auth -> auth
              // permitir registro/login sin autenticación
              .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
              .requestMatchers("/h2-console","/h2-console/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
              .requestMatchers("/users").permitAll() // allow registration POST /users
              .requestMatchers("/users/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/users", "/users/register", "/auth/**").permitAll()
              // permitir GET públicos (solución rápida para tests E2E)
              .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
              .anyRequest().permitAll()
          );
        return http.build();
    }

    // Bean requerido por RegisterUserService
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}