package ru.greenpix.messenger.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInDto {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

}
