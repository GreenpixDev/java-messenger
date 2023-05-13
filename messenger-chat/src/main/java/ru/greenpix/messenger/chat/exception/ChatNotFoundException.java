package ru.greenpix.messenger.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда чат не найден.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "chat not found")
public class ChatNotFoundException extends RuntimeException {
}
