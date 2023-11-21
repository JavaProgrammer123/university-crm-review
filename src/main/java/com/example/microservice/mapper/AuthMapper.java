package com.example.microservice.mapper;

import com.example.microservice.entity.User;
import com.example.microservice.repository.UserRepository;
import com.example.microservice.security.dto.AuthResponseDto;
import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UserRepository.class, UUID.class, User.class})
public abstract class AuthMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(expression = "java(getUserId(email))", target = "id")
    @Mapping(source = "accessTokenResponse.token", target = "accessToken")
    public abstract AuthResponseDto mapKeycloakDtoToAuthResponseDto(AccessTokenResponse accessTokenResponse, String email);

    @Mapping(expression = "java(null)", target = "id")
    @Mapping(source = "accessTokenResponse.token", target = "accessToken")
    public abstract AuthResponseDto mapKeycloakDtoToAuthResponseDto(AccessTokenResponse accessTokenResponse);

    protected final UUID getUserId(String email) {
        return userRepository.findUserByMail(email).orElse(new User()).getId();
    }
}
