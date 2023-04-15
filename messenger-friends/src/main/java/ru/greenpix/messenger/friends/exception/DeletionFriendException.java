package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда не получилось удалить пользователя из друзей.
 *
 * @see ru.greenpix.messenger.friends.service.impl.FriendServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "the user isn't yet friend the specified user")
public class DeletionFriendException extends RuntimeException {
}
