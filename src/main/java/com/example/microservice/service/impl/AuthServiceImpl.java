package com.example.microservice.service.impl;

import com.example.microservice.mapper.AuthMapper;
import com.example.microservice.repository.UserRepository;
import com.example.microservice.security.dto.AuthRequestDTO;
import com.example.microservice.security.dto.AuthResponseDto;
import com.example.microservice.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.example.microservice.exception.NotFoundMessage.USER_NOT_FOUND;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${keycloak.auth-server-url}")
    private String keyCloakUrl;

    @Value("${utils.keycloak.auth-segment}")
    private String authSegment;

    private final RestTemplate restTemplate;

    private final MultiValueMap<String, String> refreshParameters;

    private final MultiValueMap<String, String> authParameters;

    private final AuthMapper authMapper;

    private final UserRepository userRepository;

    @Override
    public AuthResponseDto authenticate(AuthRequestDTO request, String clientId, String realm) {
        userRepository.findUserByMail(request.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND));

        return authenticate(request.getUsername(), request.getPassword(), clientId, realm);
    }

    public final AuthResponseDto authenticate(String login, String password, String clientId, String realm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var authParameters = new LinkedMultiValueMap<>(this.authParameters);
        formatParams(authParameters, login, "login");
        formatParams(authParameters, password, "pass");
        formatParams(authParameters, clientId, "clientId");

        var entity = new HttpEntity<>(authParameters, headers);

        String url = String.format(UriComponentsBuilder
                        .fromHttpUrl(keyCloakUrl + authSegment)
                        .toUriString(),
                realm);
        AccessTokenResponse accessTokenResponse = restTemplate.postForObject(url, entity, AccessTokenResponse.class);
        return authMapper.mapKeycloakDtoToAuthResponseDto(accessTokenResponse, login);
    }

    @Override
    public AuthResponseDto createRefreshToken(String refreshToken, String clientId, String realm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var refreshParameters = new LinkedMultiValueMap<>(this.refreshParameters);
        formatParams(refreshParameters, clientId, "clientId");
        formatParams(refreshParameters, refreshToken, "refreshToken");

        var entity = new HttpEntity<>(refreshParameters, headers);

        String url = String.format(UriComponentsBuilder
                        .fromHttpUrl(keyCloakUrl + authSegment)
                        .toUriString(),
                realm);
        AccessTokenResponse accessTokenResponse = restTemplate.postForObject(url, entity, AccessTokenResponse.class);
        return authMapper.mapKeycloakDtoToAuthResponseDto(accessTokenResponse);
    }

    private void formatParams(MultiValueMap<String, String> map, String value, String defaultValue) {
        map.replaceAll((s, list) -> {
            if (list.contains(defaultValue)) {
                return List.of(value);
            }
            return list;
        });
    }

    @Autowired
    public AuthServiceImpl(RestTemplate restTemplate, AuthMapper authMapper, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.authMapper = authMapper;
        this.userRepository = userRepository;

        authParameters = new LinkedMultiValueMap<>();
        authParameters.set("username", "login");
        authParameters.set("password", "pass");
        authParameters.set("grant_type", "password");
        authParameters.set("client_id", "clientId");

        refreshParameters = new LinkedMultiValueMap<>();
        refreshParameters.set("grant_type", "refresh_token");
        refreshParameters.set("client_id", "clientId");
        refreshParameters.set("refresh_token", "refreshToken");
    }

}

