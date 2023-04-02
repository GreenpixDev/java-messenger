package ru.greenpix.messenger.user.dto;

import lombok.Data;
import ru.greenpix.messenger.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link User} entity
 */
@Data
public class UserResponseDto implements Serializable {
    private final UUID id;
    private final String username;
    private final String fullName;
    private final LocalDateTime registrationTimestamp;
    private final LocalDate birthDate;
}