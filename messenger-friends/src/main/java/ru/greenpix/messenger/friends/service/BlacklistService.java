package ru.greenpix.messenger.friends.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
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
     * @throws ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException target пользователь не заблокировал blockedUser пользователя
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     * @return заблокированный пользователь
     */
    @NotNull
    BlockedUser getBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод добавления пользователя в черный список
     * @throws ru.greenpix.messenger.friends.exception.AdditionBlockedUserException target пользователь уже заблокировал blockedUser пользователя
     * @throws ru.greenpix.messenger.common.exception.UserNotFoundException пользовать с ID friendUserId не найден в микросервисе "Пользователи"
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void addBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод удаления пользователя из черного списка
     * @throws ru.greenpix.messenger.friends.exception.DeletionBlockedUserException target пользователь ещё не заблокировал blockedUser пользователя
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void deleteBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод синхронизации данных заблокированного пользователя (например, ФИО) с внешними микросервисами.
     * Позволяет подтянуть изменения из других микросервисов.
     * Например, если ФИО заблокированного пользователя было изменено в микросервисе "Пользователь", то после
     * вызова данного метода ФИО изменится и в данном микросервисе.
     * @throws ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException target пользователь не дружит с friend пользователем
     * @throws ru.greenpix.messenger.common.exception.UserNotFoundException пользовать с ID friendUserId не найден в микросервисе "Пользователи"
     * @param targetUserId ID целевого пользователя
     * @param blockedUserId ID заблокированного пользователя
     */
    void synchronizeBlockedUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

    /**
     * Метод поиска пользователей черного списка
     * @param targetUserId ID целевого пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param searchDto фильтр поиска
     * @return страница заблокированных пользователей
     */
    @NotNull
    Page<BlockedUser> getBlockedUserPage(@NotNull UUID targetUserId, int page, int size, @NotNull BlockedUserSearchDto searchDto);

    boolean isBlockedByUser(@NotNull UUID targetUserId, @NotNull UUID blockedUserId);

}
