package com.example.microservice.service;

import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.UniversityResponseDTO;

import java.util.List;

public interface UniversityService {

    UniversityResponseDTO createUniversity(UniversityRequestDTO universityRequestDTO);

    List<UniversityResponseDTO> getAll();

    List<UniversityResponseDTO> getAllByCity(String city);
}
