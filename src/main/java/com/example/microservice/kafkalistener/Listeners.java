package com.example.microservice.kafkalistener;

import com.example.microservice.dto.response.UniversityResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listeners {

    @KafkaListener(topics = "topic")
    public void listener(UniversityResponseDTO message) {
        System.out.println(message.getCity()); // тестовое действие с объектом
    }
}
