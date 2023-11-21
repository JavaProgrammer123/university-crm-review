package com.example.microservice.mapper;

import com.example.microservice.dto.request.UserRequestDTO;
import com.example.microservice.dto.response.UserResponseDTO;
import com.example.microservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class})
public abstract class UserMapper {

    @Mapping(source = "id", target = "id")
    public abstract User mapUserDtoToEntity(UserRequestDTO userRequestDto, UUID id);

    public abstract UserResponseDTO entityToResponse(User user);
}
