package ru.greenpix.messenger.user.service;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.UserNotFoundException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;

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
     * Получает список пользователей
     * @return список моделей пользователей
     */
    @NotNull
    List<User> getUsers();

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
