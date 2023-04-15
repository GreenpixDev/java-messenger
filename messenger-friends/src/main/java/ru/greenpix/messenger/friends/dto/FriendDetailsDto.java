package ru.greenpix.messenger.friends.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO for the {@link ru.greenpix.messenger.friends.entity.Friend} entity
 */
@Data
public class FriendDetailsDto implements Serializable {
    private final UUID friendUserId;
    private final UUID targetUserId;
    private final String fullName;
    private final LocalDate additionDate;
    private final LocalDate deletionDate;
}