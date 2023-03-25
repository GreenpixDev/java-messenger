package ru.greenpix.messenger.auth.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequestDto {

    private final String username;
    private final String fullName;
    private final LocalDate birthDate;
    private final String password;

}
