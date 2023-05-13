package ru.greenpix.messenger.chat.integration.users.client;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UsersClient {

    /**
     * Метод получения пользователей с сервиса "Пользователи"
     * @param userIds коллекция идентификаторов искомых пользователей
     * @return список пользователей
     */
    @NotNull
    List<UserIntegrationDto> getUsers(@NotNull Collection<UUID> userIds);

}
