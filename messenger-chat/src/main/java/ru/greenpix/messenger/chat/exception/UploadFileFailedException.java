package ru.greenpix.messenger.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY, reason = "upload fail failed")
public class UploadFileFailedException extends RuntimeException {
}
