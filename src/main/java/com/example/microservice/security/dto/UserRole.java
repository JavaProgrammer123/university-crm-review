package com.example.microservice.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole {

    private boolean clientRole;

    private boolean composite;

    private String containerId = "crm_system";

    private String id;

    private String name;

    public UserRole(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

