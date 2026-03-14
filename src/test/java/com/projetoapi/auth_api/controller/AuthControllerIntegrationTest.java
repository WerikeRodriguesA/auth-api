package com.projetoapi.auth_api.controller;

import com.projetoapi.auth_api.dto.request.LoginRequest;
import com.projetoapi.auth_api.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient() {
        return RestClient.create("http://localhost:" + port);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        RegisterRequest request = new RegisterRequest(
                "Teste Usuario",
                "teste@email.com",
                "12345678"
        );

        ResponseEntity<Map> response = restClient()
                .post()
                .uri("/auth/register")
                .body(request)
                .retrieve()
                .toEntity(Map.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().get("accessToken"));
        assertNotNull(response.getBody().get("refreshToken"));
    }

    @Test
    void deveRejeitarRegistroComEmailInvalido() {
        RegisterRequest request = new RegisterRequest(
                "Teste Usuario",
                "emailinvalido",
                "12345678"
        );

        try {
            restClient()
                    .post()
                    .uri("/auth/register")
                    .body(request)
                    .retrieve()
                    .toEntity(Map.class);
            fail("Deveria ter lançado exceção");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("400") || e.getMessage().contains("Bad Request"));
        }
    }

    @Test
    void deveLoginComSucesso() {
        RegisterRequest register = new RegisterRequest(
                "Teste Usuario",
                "login@email.com",
                "12345678"
        );
        restClient().post().uri("/auth/register").body(register).retrieve().toEntity(Map.class);

        LoginRequest login = new LoginRequest("login@email.com", "12345678");

        ResponseEntity<Map> response = restClient()
                .post()
                .uri("/auth/login")
                .body(login)
                .retrieve()
                .toEntity(Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("accessToken"));
    }

    @Test
    void deveRejeitarLoginComCredenciaisErradas() {
        LoginRequest login = new LoginRequest("inexistente@email.com", "senhaerrada");

        try {
            restClient()
                    .post()
                    .uri("/auth/login")
                    .body(login)
                    .retrieve()
                    .toEntity(Map.class);
            fail("Deveria ter lançado exceção");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("401") || e.getMessage().contains("Unauthorized"));
        }
    }
}