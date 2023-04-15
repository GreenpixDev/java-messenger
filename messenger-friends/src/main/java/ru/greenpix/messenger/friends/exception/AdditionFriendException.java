package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда друг не найден.
 *
 * @see ru.greenpix.messenger.friends.service.impl.FriendServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "the user is already friends with the specified user")
public class AdditionFriendException extends RuntimeException {
}
