package com.example.microservice.dto.response;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class UniversityResponseDTO {

    private String city;

    private String name;
}
