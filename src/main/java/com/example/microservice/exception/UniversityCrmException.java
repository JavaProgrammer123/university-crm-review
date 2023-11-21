package com.example.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UniversityCrmException extends RuntimeException {
    public UniversityCrmException(String message) {
        super(message);
    }
}
