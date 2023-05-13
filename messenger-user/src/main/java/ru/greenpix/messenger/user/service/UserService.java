package ru.greenpix.messenger.user.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.dto.UserSortListDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса пользователей
 */
public interface UserService {

    /**
     * Метод регистрации пользователя в системе
     * @throws DuplicateUsernameException пользователя уже зарегистрирован с указанным в signUpDto логином (username'ом)
     * @param signUpDto DTO регистрации в системе
     * @return модель пользователя
     */
    @NotNull
    User registerUser(@NotNull SignUpDto signUpDto);

    /**
     * Метод аутентификации пользователя в системе
     * @throws WrongCredentialsException неверный логин или пароль
     * @param signInDto DTO входа в систему
     * @return модель пользователя
     */
    @NotNull
    User authenticateUser(@NotNull SignInDto signInDto);

    /**
     * Получает страницу пользователей
     * @param page номер страницы
     * @param size размер страницы
     * @param sorts сортировки
     * @param filters фильтры
     * @return страница моделей пользователей
     */
    @NotNull
    Page<User> getUsers(int page, int size, @NotNull UserSortListDto sorts, @NotNull UserFilterListDto filters);

    /**
     * Получает пользователя по его логину
     * @throws UserNotFoundException пользователь не найден
     * @param username логин пользователя
     * @return модель искомого пользователя
     */
    @NotNull
    User getUser(@NotNull String username);

    /**
     * Получает пользователя по его ID
     * @throws UserNotFoundException пользователь не найден
     * @param userId ID пользователя
     * @return модель искомого пользователя
     */
    @NotNull
    User getUser(@NotNull UUID userId);

    /**
     * Получает пользователей по их ID
     * @param userIds коллекция ID пользователей
     * @return модель искомого пользователя
     */
    @NotNull
    List<User> getUsers(@NotNull Collection<UUID> userIds);

    /**
     * Получает искомого пользователя по его ID от лица пользователя, которые делает запрос
     * @throws UserNotFoundException пользователь не найден
     * @throws ru.greenpix.messenger.user.exception.BlacklistUserAccessRestrictionException искомый пользователь заблокировал пользователя, который делал запрос
     * @param requesterId ID пользователя, который делает запрос
     * @param requestedUserId ID искомого (запрашиваемого) пользователя
     * @return модель искомого пользователя
     */
    @NotNull
    User getUser(@NotNull UUID requesterId, @NotNull UUID requestedUserId);

    /**
     * Обновляет профиль пользователя
     * @throws UserNotFoundException пользователь не найден
     * @param userId ID пользователя
     * @param userRequestDto DTO обновления профиля пользователя
     * @return модель обновленного пользователя
     */
    @NotNull
    User updateUser(@NotNull UUID userId, @NotNull UserRequestDto userRequestDto);

}
