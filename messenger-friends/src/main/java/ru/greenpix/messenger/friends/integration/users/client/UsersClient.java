package ru.greenpix.messenger.friends.integration.users.client;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

    /**
     * Метод получения ФИО пользователя с сервиса "Пользователи"
     * @param userId ID искомого пользователя
     * @return ФИО искомого пользователя (опционально)
     */
    @NotNull
    Optional<String> getUserFullName(@NotNull UUID userId);

}
