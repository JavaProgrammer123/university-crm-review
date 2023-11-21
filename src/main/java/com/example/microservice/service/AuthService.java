package com.example.microservice.service;

import com.example.microservice.security.dto.AuthRequestDTO;
import com.example.microservice.security.dto.AuthResponseDto;

public interface AuthService {

    AuthResponseDto authenticate(AuthRequestDTO request, String clientId, String realm);

    AuthResponseDto createRefreshToken(String refreshToken, String clientId, String realm);


}
