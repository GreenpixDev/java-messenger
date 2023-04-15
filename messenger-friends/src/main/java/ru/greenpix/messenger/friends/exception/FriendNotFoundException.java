package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда пользователь не найден в списке друзей.
 *
 * @see ru.greenpix.messenger.friends.service.impl.FriendServiceImpl
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "friend not found")
public class FriendNotFoundException extends RuntimeException {
}
