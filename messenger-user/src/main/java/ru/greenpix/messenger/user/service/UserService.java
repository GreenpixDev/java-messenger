package ru.greenpix.messenger.user.service;

import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса пользователей
 */
public interface UserService {

    /**
     * Метод регистрации пользователя в системе
     * @param signUpDto DTO входа в систему
     * @return модель пользователя
     */
    User registerUser(SignUpDto signUpDto);

    User authenticateUser(SignInDto signInDto);

    List<User> getUsers();

    User getUser(String username);

    User getUser(UUID userId);

    User updateUser(UUID userId, UserRequestDto userRequestDto);

}
