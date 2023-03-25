package ru.greenpix.messenger.auth.controller.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private final String username;
    private final String fullName;

}
