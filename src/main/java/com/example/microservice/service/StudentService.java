package com.example.microservice.service;

import com.example.microservice.dto.request.StudentRequestDTO;
import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.StudentResponseDTO;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO);

    StudentResponseDTO getStudentById(Long id);

    List<StudentResponseDTO> getAllStudent(String name);

    StudentResponseDTO addUniversity(Long id, UniversityRequestDTO universityRequestDTO);

    void deleteStudent(Long id);
}
