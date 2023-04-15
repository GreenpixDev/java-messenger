package ru.greenpix.messenger.common.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleFailedValidation(
            ConstraintViolationException e
    ) {
        return ResponseEntity.badRequest().body(Map.of(
           "message", e.getMessage()
        ));
    }

}
