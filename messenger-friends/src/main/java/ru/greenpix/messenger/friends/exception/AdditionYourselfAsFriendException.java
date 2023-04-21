package ru.greenpix.messenger.friends.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда происходит попытка добавить себя в друзья.
 *
 * @see ru.greenpix.messenger.friends.service.impl.FriendServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "cannot add yourself as a friend")
public class AdditionYourselfAsFriendException extends RuntimeException {
}
