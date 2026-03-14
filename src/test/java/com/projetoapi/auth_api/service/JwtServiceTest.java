package com.projetoapi.auth_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "test-secret-key-for-tests-only-min-256-bits-xxxxxxxxxxx");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 900000L);

        userDetails = User.withUsername("werike@email.com")
                .password("senha123")
                .authorities(List.of())
                .build();
    }

    @Test
    void deveGerarTokenValido() {
        String token = jwtService.generateAccessToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairEmailDoToken() {
        String token = jwtService.generateAccessToken(userDetails);
        String email = jwtService.extractEmail(token);
        assertEquals("werike@email.com", email);
    }

    @Test
    void deveValidarTokenCorreto() {
        String token = jwtService.generateAccessToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void deveRejeitarTokenDeOutroUsuario() {
        String token = jwtService.generateAccessToken(userDetails);

        UserDetails outroUsuario = User.withUsername("outro@email.com")
                .password("senha123")
                .authorities(List.of())
                .build();

        assertFalse(jwtService.isTokenValid(token, outroUsuario));
    }
}