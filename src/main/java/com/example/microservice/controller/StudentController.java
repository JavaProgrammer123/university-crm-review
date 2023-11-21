package com.example.microservice.controller;

import com.example.microservice.dto.request.StudentRequestDTO;
import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.StudentResponseDTO;
import com.example.microservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/create")
    public StudentResponseDTO create(@RequestBody StudentRequestDTO studentRequestDTO) {
        return studentService.createStudent(studentRequestDTO);
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/all")
    public List<StudentResponseDTO> getAllStudent(@RequestParam String name) {
        return studentService.getAllStudent(name);
    }

   @PutMapping("/addUniversity/{id}")
   public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id,
                                                           @RequestBody UniversityRequestDTO universityRequestDTO) {
             return ResponseEntity.ok().body(studentService.addUniversity(id, universityRequestDTO));
   }

   @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
   }
}
