package ru.greenpix.messenger.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда пользователь не найден.
 *
 * @see ru.greenpix.messenger.user.service.impl.UserServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException {
}
