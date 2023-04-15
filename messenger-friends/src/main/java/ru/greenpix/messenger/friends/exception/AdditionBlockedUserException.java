package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда не получилось добавить пользователя в черный список.
 *
 * @see ru.greenpix.messenger.friends.service.impl.BlacklistServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "the user is already in blacklist with the specified user")
public class AdditionBlockedUserException extends RuntimeException {
}
