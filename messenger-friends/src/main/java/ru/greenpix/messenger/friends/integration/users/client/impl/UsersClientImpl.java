package ru.greenpix.messenger.friends.integration.users.client.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.settings.IntegrationSettings;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsersClientImpl implements UsersClient {

    private final RestTemplate restTemplate;
    private final IntegrationSettings integrationSettings;

    @Override
    public @NotNull Optional<String> getUserFullName(@NotNull UUID userId) {
        String url = integrationSettings.getUsersServiceUrl() + "/api/users/" + userId + "/full-name";
        try {
            return Optional.ofNullable(restTemplate.getForEntity(URI.create(url), String.class).getBody());
        }
        catch (HttpClientErrorException ignored) {
            return Optional.empty();
        }
    }
}
