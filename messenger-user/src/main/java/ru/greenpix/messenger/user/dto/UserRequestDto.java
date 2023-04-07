package ru.greenpix.messenger.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRequestDto {
    private final String fullName;
    private final LocalDate birthDate;
    private final String phone;
    private final String city;
    private final UUID avatarId;
}