package com.example.microservice.controller;

import com.example.microservice.security.dto.AuthRequestDTO;
import com.example.microservice.security.dto.AuthResponseDto;
import com.example.microservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.realm}")
    private String realm;

    private final AuthService authService;

    @PostMapping("/log")
    public ResponseEntity<AuthResponseDto> auth(@RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(authRequestDTO, clientId, realm));
    }

}
