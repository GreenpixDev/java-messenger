package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда происходит заблокировать самого себя.
 *
 * @see ru.greenpix.messenger.friends.service.impl.BlacklistServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "cannot block yourself")
public class AdditionYourselfAsBlockedUserException extends RuntimeException {
}
