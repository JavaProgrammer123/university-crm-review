package com.example.microservice.service;

import com.example.microservice.containers.DataBaseContainer;
import com.example.microservice.dto.response.UniversityResponseDTO;
import com.example.microservice.entity.University;
import com.example.microservice.mapper.UniversityMapper;
import com.example.microservice.repository.UniversityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UniversityServiceTest {

    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private UniversityService universityService;

    @Container
    public static PostgreSQLContainer<DataBaseContainer> dbContainer = DataBaseContainer.getInstance();

    @Test
    void getByCity() {
        Assertions.assertEquals(List.of(dto()), universityService.getAllByCity("Пенза"));
    }

    private UniversityResponseDTO dto() {
        return universityMapper.entityToResponse(getUniversity());
    }

    private University getUniversity() {
        return  University.builder()
                .id(1L)
                .city("Пенза")
                .name("ПГУАС")
                .build();
    }
}
