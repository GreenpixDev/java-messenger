package ru.greenpix.messenger.user.integration.friends.client;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface FriendsClient {

    boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

}
