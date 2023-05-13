package ru.greenpix.messenger.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда чат не соответствует типу (например, не групповой).
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "illegal chat type")
public class IllegalChatTypeException extends RuntimeException {
}
