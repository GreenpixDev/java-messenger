package ru.greenpix.messenger.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда возникает попытка создать уже
 * существующего в системе пользователя.
 *
 * @see ru.greenpix.messenger.user.service.impl.UserServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "user already registered")
public class DuplicateUsernameException extends RuntimeException {
}
