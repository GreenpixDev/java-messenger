package ru.greenpix.messenger.friends.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO for the {@link ru.greenpix.messenger.friends.entity.BlockedUser} entity
 */
@Data
public class BlockedUserDetailsDto implements Serializable {
    private final UUID targetUserId;
    private final UUID blockedUserId;
    private final String fullName;
    private final LocalDate additionDate;
    private final LocalDate deletionDate;
}