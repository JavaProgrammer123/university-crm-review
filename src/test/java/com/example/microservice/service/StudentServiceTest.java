package com.example.microservice.service;

import com.example.microservice.containers.DataBaseContainer;
import com.example.microservice.dto.response.StudentResponseDTO;
import com.example.microservice.entity.Student;
import com.example.microservice.entity.University;
import com.example.microservice.entity.User;
import com.example.microservice.mapper.StudentMapper;
import com.example.microservice.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class StudentServiceTest {


    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

    @Container
    public static PostgreSQLContainer<DataBaseContainer> dbContainer = DataBaseContainer.getInstance();

    @Test
    void getById() {
        Assertions.assertEquals(studentService.getStudentById(1L), getResponse());
    }

    private StudentResponseDTO getResponse() {
        return studentMapper.entityToResponse(getStudent());
    }

    private Student getStudent() {
        return Student.builder()
                .id(1L)
                .groupNumber("21ИСТ2")
                .university(getUniversity())
                .user(getUser())
                .build();
    }

    private User getUser() {
        UUID uuid = UUID.fromString("54986c2c-e4c9-11ed-b5ea-0242ac120002");
        return User.builder()
                .id(uuid)
                .mail("test@mail.ru")
                .name("test")
                .surname("test")
                .phone("888888")
                .build();
    }

    private University getUniversity() {
        return  University.builder()
                .id(1L)
                .city("Пенза")
                .name("ПГУАС")
                .build();
    }
}
