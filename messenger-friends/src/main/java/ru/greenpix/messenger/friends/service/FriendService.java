package ru.greenpix.messenger.friends.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.exception.AdditionFriendException;

import java.util.UUID;

/**
 * Интерфейс сервиса друзей
 */
public interface FriendService {

    @NotNull
    Page<Friend> getFriendPage(@NotNull UUID targetUserId, int page, int size, @NotNull String fullNameFilter);


    @NotNull
    Friend getFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     *
     * @throws AdditionFriendException target пользователь уже дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     */
    void addFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     *
     * @throws ru.greenpix.messenger.friends.exception.FriendNotFoundException target пользователь ещё не дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     */
    void deleteFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

}
