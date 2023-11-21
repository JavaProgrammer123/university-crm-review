package com.example.microservice.controller;

import com.example.microservice.dto.request.UserRequestDTO;
import com.example.microservice.dto.response.UserResponseDTO;
import com.example.microservice.service.RegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> registration(@RequestBody UserRequestDTO userRequestDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(registrationService.registration(userRequestDto));
    }
}
