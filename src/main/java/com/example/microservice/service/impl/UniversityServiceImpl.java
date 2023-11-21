package com.example.microservice.service.impl;

import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.UniversityResponseDTO;
import com.example.microservice.entity.University;
import com.example.microservice.mapper.UniversityMapper;
import com.example.microservice.repository.UniversityRepository;
import com.example.microservice.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    private final KafkaTemplate<String, UniversityResponseDTO> kafkaTemplate;

    @Override
    public UniversityResponseDTO createUniversity(UniversityRequestDTO universityRequestDTO) {
        University university = universityMapper.requestToEntity(universityRequestDTO);
        universityRepository.findByCityAndName(university.getCity(), university.getName()).ifPresent(universityRepository::save);
        UniversityResponseDTO response = universityMapper.entityToResponse(university);
        kafkaTemplate.send("topic", response).addCallback(send -> log.info("отправка"), Throwable::printStackTrace);
        return response;
    }

    @Override
    public List<UniversityResponseDTO> getAll() {
        List<UniversityResponseDTO> university = universityMapper.listEntityToListResponse(universityRepository.findAll());
        return university.stream()
                .sorted(Comparator.comparing(UniversityResponseDTO::getCity))
                .collect(Collectors.toList());
    }

    @Override
    public List<UniversityResponseDTO> getAllByCity(String city) {
        return universityMapper.listEntityToListResponse(universityRepository.findAllByCity(city));
    }

}
