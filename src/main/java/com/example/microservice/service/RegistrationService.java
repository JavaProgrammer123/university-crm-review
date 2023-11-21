package com.example.microservice.service;

import com.example.microservice.dto.request.UserRequestDTO;
import com.example.microservice.dto.response.UserResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface RegistrationService {
    UserResponseDTO registration(UserRequestDTO userRequestDto) throws JsonProcessingException;
}
