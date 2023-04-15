package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда не получилось удалить пользователя из черного списка.
 *
 * @see ru.greenpix.messenger.friends.service.impl.BlacklistServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "the user isn't yet in blacklist the specified user")
public class DeletionBlockedUserException extends RuntimeException {
}
