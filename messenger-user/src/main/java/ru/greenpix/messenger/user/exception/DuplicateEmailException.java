package ru.greenpix.messenger.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда возникает попытка создать пользователя
 * в системе с занятым email'ом.
 *
 * @see ru.greenpix.messenger.user.service.impl.UserServiceImpl
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "email already registered")
public class DuplicateEmailException extends RuntimeException {
}
