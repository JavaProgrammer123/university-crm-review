package com.example.microservice.service.impl;

import com.example.microservice.dto.request.StudentRequestDTO;
import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.StudentResponseDTO;
import com.example.microservice.entity.Student;
import com.example.microservice.entity.University;
import com.example.microservice.exception.NotFoundMessage;
import com.example.microservice.mapper.StudentMapper;
import com.example.microservice.repository.StudentRepository;
import com.example.microservice.repository.UniversityRepository;
import com.example.microservice.repository.UserRepository;
import com.example.microservice.service.StudentService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;

    private final UniversityRepository universityRepository;

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentServiceImpl(UserRepository userRepository, UniversityRepository universityRepository, StudentRepository studentRepository, StudentMapper studentMapper, MeterRegistry meterRegistry) {
        this.userRepository = userRepository;
        this.universityRepository = universityRepository;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        AtomicInteger integer = new AtomicInteger();
        meterRegistry.gauge("createStudent", integer);
    }


    @Override
    @Timed(value = "createStudent")
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {
        Student student = studentMapper.requestToEntity(studentRequestDTO);
        student.setUser(userRepository.findById(studentRequestDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException(NotFoundMessage.USER_NOT_FOUND)));
        return addUniversityToStudent(student, studentRequestDTO.getUniversity());
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(NotFoundMessage.USER_NOT_FOUND)
        );
        return studentMapper.entityToResponse(student);
    }

    @Override
    public List<StudentResponseDTO> getAllStudent(String name) {
        return studentMapper.listEntityToListResponse(studentRepository.findAllByUniversityName(name));
    }

    @Override
    public StudentResponseDTO addUniversity(Long id, UniversityRequestDTO universityRequestDTO) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(NotFoundMessage.USER_NOT_FOUND)
        );
        return addUniversityToStudent(student, universityRequestDTO);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    private StudentResponseDTO addUniversityToStudent(Student student, UniversityRequestDTO universityRequestDTO) {
        University university = universityRepository.findByCityAndName(universityRequestDTO.getCity(), universityRequestDTO.getName())
                .orElseThrow(() -> new EntityNotFoundException(NotFoundMessage.UNIVERSITY_NOT_FOUND));
        student.setUniversity(university);
        return studentMapper.entityToResponse(studentRepository.save(student));
    }
}
