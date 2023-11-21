package com.example.microservice.mapper;

import com.example.microservice.dto.request.UniversityRequestDTO;
import com.example.microservice.dto.response.UniversityResponseDTO;
import com.example.microservice.entity.University;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UniversityMapper {

    public abstract UniversityResponseDTO entityToResponse(University university);

    public abstract University requestToEntity(UniversityRequestDTO universityRequestDTO);

    public abstract List<UniversityResponseDTO> listEntityToListResponse(List<University> list);

}
