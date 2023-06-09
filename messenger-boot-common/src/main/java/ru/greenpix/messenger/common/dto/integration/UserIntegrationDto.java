package ru.greenpix.messenger.common.dto.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIntegrationDto {
    private UUID id;
    private String fullName;
    private UUID avatarId;
}
