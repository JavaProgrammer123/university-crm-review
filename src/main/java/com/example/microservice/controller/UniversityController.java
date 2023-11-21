package com.example.microservice.controller;

import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.UniversityResponseDTO;
import com.example.microservice.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/university")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService service;

    @PostMapping("/create")
    public UniversityResponseDTO createUniversity(@RequestBody UniversityRequestDTO universityRequestDTO) {
        return service.createUniversity(universityRequestDTO);
    }

    @GetMapping("/all")
    public List<UniversityResponseDTO> getAllUniversity() {
        return service.getAll();
    }

    @GetMapping("/")
    public List<UniversityResponseDTO> getAllByCity(@RequestParam String city) {
        return service.getAllByCity(city);
    }

}
