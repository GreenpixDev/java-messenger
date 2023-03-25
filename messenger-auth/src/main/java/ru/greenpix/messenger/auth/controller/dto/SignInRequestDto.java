package ru.greenpix.messenger.auth.controller.dto;

import lombok.Data;

@Data
public class SignInRequestDto {

    private final String username;
    private final String password;

}
