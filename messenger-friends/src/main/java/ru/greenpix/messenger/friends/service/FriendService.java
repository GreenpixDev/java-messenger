package ru.greenpix.messenger.friends.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.Friend;

import java.util.UUID;

/**
 * Интерфейс сервиса друзей
 */
public interface FriendService {

    /**
     * Метод получения страницы друзей
     * @param targetUserId ID целевого пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param fullNameFilter фильтр по ФИО
     * @return страница друзей
     */
    @NotNull
    Page<Friend> getFriendPage(@NotNull UUID targetUserId, int page, int size, @NotNull String fullNameFilter);

    /**
     * Метод получения информации о друге
     * @throws ru.greenpix.messenger.friends.exception.FriendNotFoundException target пользователь не дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     * @return друг
     */
    @NotNull
    Friend getFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     * Метод добавления пользователя в друзья
     * @throws ru.greenpix.messenger.friends.exception.AdditionFriendException target пользователь уже дружит с friend пользователем
     * @throws ru.greenpix.messenger.friends.exception.AdditionYourselfAsFriendException когда происходит попытка добавить себя в друзья
     * @throws ru.greenpix.messenger.common.exception.UserNotFoundException пользовать с ID friendUserId не найден в микросервисе "Пользователи
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     */
    void addFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     * Метод удаления пользователя из друзей
     * @throws ru.greenpix.messenger.friends.exception.DeletionFriendException target пользователь ещё не дружит с friend пользователем
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     */
    void deleteFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     * Метод синхронизации данных друга (например, ФИО) с внешними микросервисами.
     * Позволяет подтянуть изменения из других микросервисов.
     * Например, если ФИО друга было изменено в микросервисе "Пользователь", то после
     * вызова данного метода ФИО изменится и в данном микросервисе.
     * @throws ru.greenpix.messenger.friends.exception.FriendNotFoundException target пользователь не дружит с friend пользователем
     * @throws ru.greenpix.messenger.common.exception.UserNotFoundException пользовать с ID friendUserId не найден в микросервисе "Пользователи"
     * @param targetUserId ID целевого пользователя
     * @param friendUserId ID пользователя друга
     */
    void synchronizeFriend(@NotNull UUID targetUserId, @NotNull UUID friendUserId);

    /**
     * Метод поиска друзей
     * @param targetUserId ID целевого пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param searchDto фильтры поиска
     * @return страница друзей
     */
    @NotNull
    Page<Friend> getFriendPage(@NotNull UUID targetUserId, int page, int size, @NotNull FriendSearchDto searchDto);

}
