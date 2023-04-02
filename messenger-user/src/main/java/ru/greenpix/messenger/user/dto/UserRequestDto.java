package ru.greenpix.messenger.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDto {
    private final String fullName;
    private final LocalDate birthDate;
}