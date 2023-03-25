package ru.greenpix.messenger.auth.service;

import ru.greenpix.messenger.auth.dto.SignInDto;
import ru.greenpix.messenger.auth.dto.SignUpDto;
import ru.greenpix.messenger.auth.dto.UserRequestDto;
import ru.greenpix.messenger.auth.entity.User;

public interface UserService {

    void registerUser(SignUpDto signUpDto);

    User getUser(SignInDto signInDto);

    void updateUser(SignInDto signInDto, UserRequestDto userRequestDto);

}
