package ru.greenpix.messenger.chat.integration.users.client.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.settings.IntegrationSettings;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.common.dto.integration.UserListIntegrationDto;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsersClientImpl implements UsersClient {

    private final RestTemplate restTemplate;
    private final IntegrationSettings integrationSettings;

    @Override
    public @NotNull List<UserIntegrationDto> getUsers(@NotNull Collection<UUID> userIds) {
        String url = integrationSettings.getUsersServiceUrl() + "/api/users/search";
        try {
            return Objects.requireNonNull(restTemplate.postForEntity(
                    URI.create(url),
                    userIds,
                    UserListIntegrationDto.class
            ).getBody()).getUsers();
        }
        catch (HttpClientErrorException ignored) {
            return Collections.emptyList();
        }
    }
}
