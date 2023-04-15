package ru.greenpix.messenger.friends.integration.users.client;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

    @NotNull
    Optional<String> getUserFullName(@NotNull UUID userId);

}
