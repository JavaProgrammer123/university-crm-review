package com.example.microservice.service.scheduler;

import com.example.microservice.dto.response.UniversityResponseDTO;
import com.example.microservice.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    public static final String TOPIC = "topic";

    private final KafkaTemplate<String, UniversityResponseDTO> kafkaTemplate;

    private final UniversityService universityService;

    @Scheduled(fixedRate = 120000)
    public void sendMessage() {
        universityService.getAll().forEach(universityResponseDTO ->
                kafkaTemplate.send(TOPIC, universityResponseDTO).addCallback(send -> log.info("отправка сообщения"),
                        Throwable::printStackTrace)
        );
    }
}
