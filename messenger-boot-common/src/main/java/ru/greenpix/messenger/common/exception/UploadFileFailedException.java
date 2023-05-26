package ru.greenpix.messenger.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение: произошла ошибка загрузки файла
 */
@ResponseStatus(value = HttpStatus.BAD_GATEWAY, reason = "upload fail failed")
public class UploadFileFailedException extends RuntimeException {
}
