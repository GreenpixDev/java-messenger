package ru.greenpix.messenger.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link ru.greenpix.messenger.user.entity.User} entity
 */
@Data
public class UserResponseDto implements Serializable {
    private final UUID id;
    private final LocalDateTime registrationTimestamp;
    private final String username;
    private final String email;
    private final String fullName;
    private final LocalDate birthDate;
    private final String phone;
    private final String city;
    private final UUID avatarId;
}