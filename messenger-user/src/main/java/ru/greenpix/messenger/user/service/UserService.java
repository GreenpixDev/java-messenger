package ru.greenpix.messenger.user.service;

import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;

public interface UserService {

    void registerUser(SignUpDto signUpDto);

    User getUser(SignInDto signInDto);

    void updateUser(SignInDto signInDto, UserRequestDto userRequestDto);

}
