package ru.greenpix.messenger.user.integration.friends.client;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface FriendsClient {

    /**
     * Проверка, заблокирован ли пользователь в сервисе "Друзей"
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID потенциально заблокированного пользователя
     * @return true, если потенциальный пользователь заблокирован
     */
    boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

}
