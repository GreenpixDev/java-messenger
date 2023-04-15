package ru.greenpix.messenger.friends.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.friends.entity.BlockedUser;

import java.util.UUID;

/**
 * Интерфейс сервиса друзей
 */
public interface BlacklistService {

    /**
     * Метод получения страницы пользователей черного списка
     * @param targetUserId ID целевого пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param fullNameFilter фильтр по ФИО
     * @return страница заблокированных пользователей
     */
    @NotNull
    Page<BlockedUser> getBlockedUserPage(@NotNull UUID targetUserId, int page, int size, @NotNull String fullNameFilter);

    /**
     * Метод получения информации о пользователе в черном списке
     * @throws ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException target пользователь уже дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     * @return заблокированный пользователь
     */
    @NotNull
    BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод добавления пользователя в черный список
     * @throws ru.greenpix.messenger.friends.exception.AdditionBlockedUserException target пользователь уже дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод удаления пользователя из черного списка
     * @throws ru.greenpix.messenger.friends.exception.DeletionBlockedUserException target пользователь ещё не дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

}
