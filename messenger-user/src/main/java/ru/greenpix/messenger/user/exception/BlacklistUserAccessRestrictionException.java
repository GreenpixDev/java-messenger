package ru.greenpix.messenger.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое возвращается, когда первый пользователь пытается
 * посмотреть профиль второго пользователя, который заблокировал первого.
 *
 * @see ru.greenpix.messenger.user.service.impl.UserServiceImpl
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "blacklist access restriction")
public class BlacklistUserAccessRestrictionException extends RuntimeException {
}
