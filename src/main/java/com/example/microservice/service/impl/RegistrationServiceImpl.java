package com.example.microservice.service.impl;

import com.example.microservice.dto.request.RegistrationKeycloakRequestDTO;
import com.example.microservice.dto.request.UserRequestDTO;
import com.example.microservice.dto.response.UserKeycloakResponseDTO;
import com.example.microservice.dto.response.UserResponseDTO;
import com.example.microservice.entity.User;
import com.example.microservice.mapper.RegistrationMapper;
import com.example.microservice.mapper.UserMapper;
import com.example.microservice.repository.UserRepository;
import com.example.microservice.security.dto.AuthResponseDto;
import com.example.microservice.security.dto.UserRole;
import com.example.microservice.service.RegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static com.example.microservice.exception.NotFoundMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${utils.keycloak.realm-segment}")
    private String registrationSegment;

    @Value("${utils.keycloak.role-segment}")
    private String roleSegment;

    @Value("${utils.keycloak.get-role-segment}")
    private String getRoleSegment;

    @Value("${utils.keycloak.admin-client}")
    private String ADMIN_CLIENT_ID;

    @Value("${utils.keycloak.admin-realm}")
    private String REALM_MASTER;

    @Value("${utils.keycloak.admin-login}")
    private String ADMIN_LOGIN;

    @Value("${utils.keycloak.admin-password}")
    private String ADMIN_PASSWORD;

    private static final String HEADER_NAME_AUTHORISATION = "Authorization";

    private static final String token = "Bearer %s";

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    private final RegistrationMapper registrationMapper;

    private AuthResponseDto accessTokenResponse;

    private final AuthServiceImpl authService;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @PostConstruct
    private void authenticateFromAdmin() {
        accessTokenResponse = authService.authenticate(ADMIN_LOGIN, ADMIN_PASSWORD, ADMIN_CLIENT_ID, REALM_MASTER);
    }

    @Override
    public UserResponseDTO registration(UserRequestDTO userRequestDTO) throws JsonProcessingException {
        String url = String.format(UriComponentsBuilder
                        .fromHttpUrl(keycloakUrl + registrationSegment)
                        .toUriString(),
                realm);
        RegistrationKeycloakRequestDTO value = registrationMapper
                .mapDtoToKeycloakDto(userRequestDTO);
        restTemplate.postForEntity(url,
                new HttpEntity<>(objectMapper
                        .writeValueAsString(value),
                        createHeadersWithRefreshToken(accessTokenResponse.getRefreshToken())),
                Void.class);

        String userId = getUserId(userRequestDTO.getMail());
        String userRoleId = getRoleId(userId, userRequestDTO.getRealmRoles());
        String body = objectMapper.writeValueAsString(
                List.of(new UserRole(userRoleId, userRequestDTO.getRealmRoles())));

        var entityRole = new HttpEntity<>(body,
                createHeadersWithRefreshToken(accessTokenResponse.getRefreshToken()));

        url = String.format(UriComponentsBuilder
                        .fromHttpUrl(keycloakUrl + registrationSegment + roleSegment)
                        .toUriString(),
                realm, userId);

        User user = userMapper.mapUserDtoToEntity(userRequestDTO, UUID.fromString(userId));
        userRepository.saveAndFlush(user);

        restTemplate.postForEntity(url, entityRole, Void.class);

        return userMapper.entityToResponse(user);
    }

    private HttpHeaders createHeadersWithRefreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        accessTokenResponse = authService.createRefreshToken(refreshToken, ADMIN_CLIENT_ID, REALM_MASTER);
        headers.add(HEADER_NAME_AUTHORISATION, String.format(token, accessTokenResponse.getAccessToken()));
        return headers;
    }

    private String getUserId(String email) throws JsonProcessingException {
        String url = String.format(
                UriComponentsBuilder
                        .fromHttpUrl(keycloakUrl + registrationSegment)
                        .toUriString(),
                realm);

        String users = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(null, createHeadersWithRefreshToken(accessTokenResponse.getRefreshToken())),
                String.class).getBody();
        List<UserKeycloakResponseDTO> userKeycloakResponseDTOList = Arrays.asList(objectMapper.readValue(
                users, UserKeycloakResponseDTO[].class));
        return findIdFromList(userKeycloakResponseDTOList, userKeycloakResponseDTO -> userKeycloakResponseDTO.getEmail().equals(email))
                .orElseThrow(
                        () -> new EntityNotFoundException(USER_NOT_FOUND)
                ).getId();
    }

    private <T> Optional<T> findIdFromList(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst();
    }

    public String getRoleId(String userId, String userRole) throws JsonProcessingException {
        String url = String.format(
                UriComponentsBuilder
                        .fromHttpUrl(keycloakUrl + registrationSegment + roleSegment + getRoleSegment)
                        .toUriString(),
                realm, userId);

        String roles = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(null, createHeadersWithRefreshToken(accessTokenResponse.getRefreshToken())),
                String.class).getBody();
        List<UserRole> userRoleList = Arrays.asList(objectMapper.readValue(roles, UserRole[].class));

        return findIdFromList(userRoleList, role -> role.getName().equals(userRole))
                .orElseThrow(
                        () -> new EntityNotFoundException(USER_NOT_FOUND)
                ).getId();
    }

}


