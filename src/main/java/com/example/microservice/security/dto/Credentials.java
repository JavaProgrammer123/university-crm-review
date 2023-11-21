package com.example.microservice.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

    private String type = "password";

    private String value;

    private boolean temporary = false;

    public Credentials(String value) {
        this.value = value;
    }
}

