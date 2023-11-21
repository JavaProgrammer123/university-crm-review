package com.example.microservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String surname;

    private String name;

    private String patronymic;

    private String mail;

    private String phone;

    private String password;

    private String realmRoles;

}
