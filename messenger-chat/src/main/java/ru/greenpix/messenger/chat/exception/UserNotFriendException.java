package ru.greenpix.messenger.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение: пользователь не найден
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "user not friend")
public class UserNotFriendException extends RuntimeException {
}
