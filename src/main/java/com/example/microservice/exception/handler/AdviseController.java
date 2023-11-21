package com.example.microservice.exception.handler;

import com.example.microservice.exception.UniversityCrmException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class AdviseController {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(EntityNotFoundException exception) {
        return createResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UniversityCrmException.class)
    public ResponseEntity<ErrorDto> handleUniversityCrmException(UniversityCrmException exception) {
        return createResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorDto> createResponseEntity(String message, int code, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(
                        ErrorDto.builder()
                                .code(code)
                                .status(status)
                                .description(message)
                                .build()
                );
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorDto {
        private int code;
        private HttpStatus status;
        private String description;
    }

}


