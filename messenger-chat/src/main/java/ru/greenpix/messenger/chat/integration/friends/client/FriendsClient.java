package ru.greenpix.messenger.chat.integration.friends.client;

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

    /**
     * Проверка, добавлен ли в друзья пользователь в сервисе "Друзей"
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID потенциального друга
     * @return true, если потенциальный пользователь добавлен в друзья
     */
    boolean isFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

}
