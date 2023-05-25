package ru.greenpix.messenger.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение: пользователь вас заблокировал
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "user blocked")
public class UserBlockedException extends RuntimeException {
}
