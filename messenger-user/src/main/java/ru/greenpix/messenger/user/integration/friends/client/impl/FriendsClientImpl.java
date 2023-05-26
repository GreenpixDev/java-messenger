package ru.greenpix.messenger.user.integration.friends.client.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.greenpix.messenger.user.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.user.settings.IntegrationSettings;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FriendsClientImpl implements FriendsClient {

    private final RestTemplate restTemplate;
    private final IntegrationSettings integrationSettings;

    @Override
    public boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId) {
        String url = integrationSettings.getFriendsServiceUrl() + "/api/users/" + targetUserId + "/blacklist/" + blockedUserId + "/status";
        return Boolean.TRUE.equals(restTemplate.getForObject(URI.create(url), Boolean.class));
    }

}
