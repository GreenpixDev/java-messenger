package ru.greenpix.messenger.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда пользователь не найден.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user not found")
public class UserNotFoundException extends RuntimeException {
}
