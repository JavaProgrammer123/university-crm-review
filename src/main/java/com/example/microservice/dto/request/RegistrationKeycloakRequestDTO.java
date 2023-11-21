package com.example.microservice.dto.request;

import com.example.microservice.security.dto.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationKeycloakRequestDTO {

    private String username;

    private String lastName;

    private String firstName;

    private String email;

    private boolean enabled;

    private List<Credentials> credentials;
}

