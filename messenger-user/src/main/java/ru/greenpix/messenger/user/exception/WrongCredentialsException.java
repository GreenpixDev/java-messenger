package ru.greenpix.messenger.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда возникает попытка авторизации
 * с неверными данными (логином или паролем).
 *
 * @see ru.greenpix.messenger.user.service.impl.UserServiceImpl
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class WrongCredentialsException extends RuntimeException {
}
