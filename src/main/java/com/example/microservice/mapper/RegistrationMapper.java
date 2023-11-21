package com.example.microservice.mapper;

import com.example.microservice.dto.request.RegistrationKeycloakRequestDTO;
import com.example.microservice.dto.request.UserRequestDTO;
import com.example.microservice.security.dto.Credentials;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {Credentials.class, List.class})
public abstract class RegistrationMapper {

    @Mapping(expression = "java(createListCredentials(userRequestDTO.getPassword()))", target = "credentials")
    @Mapping(source = "mail", target = "email")
    @Mapping(source = "mail", target = "username")
    @Mapping(constant = "true", target = "enabled")
    public abstract RegistrationKeycloakRequestDTO mapDtoToKeycloakDto(UserRequestDTO userRequestDTO);


    protected final List<Credentials> createListCredentials(String password) {
        return List.of(new Credentials(password));
    }

}
