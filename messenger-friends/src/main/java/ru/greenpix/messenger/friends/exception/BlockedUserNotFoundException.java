package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда  пользователь не найден в черном списке.
 *
 * @see ru.greenpix.messenger.friends.service.impl.BlacklistServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "blocked user not found")
public class BlockedUserNotFoundException extends RuntimeException {
}
