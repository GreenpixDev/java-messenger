package ru.greenpix.messenger.common.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * Обработчик ошибок, связанный с валидацией
 */
@ControllerAdvice
public class ValidationAdvice {

    /**
     * Обработка ошибок валидации
     * @param e {@link ConstraintViolationException}
     * @return ResponseEntity, которая содержит поле `message` с сообщением об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleFailedValidation(
            ConstraintViolationException e
    ) {
        return ResponseEntity.badRequest().body(Map.of(
           "message", e.getMessage()
        ));
    }

}
