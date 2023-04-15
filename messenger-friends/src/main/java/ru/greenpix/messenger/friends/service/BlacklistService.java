package ru.greenpix.messenger.friends.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.exception.AdditionFriendException;

import java.util.UUID;

/**
 * Интерфейс сервиса друзей
 */
public interface BlacklistService {

    @NotNull
    Page<BlockedUser> getBlockedUserPage(@NotNull UUID targetUserId, int page, int size, @NotNull String fullNameFilter);


    @NotNull
    BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     *
     * @throws AdditionFriendException target пользователь уже дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     *
     * @throws ru.greenpix.messenger.friends.exception.FriendNotFoundException target пользователь ещё не дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

}
