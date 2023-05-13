package ru.greenpix.messenger.common.dto.integration;

import lombok.Data;

import java.util.UUID;

@Data
public class UserIntegrationDto {
    private final UUID id;
    private final String fullName;
    private final UUID avatarId;
}
