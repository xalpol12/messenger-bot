package com.xalpol12.messengerbot.messengerplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessengerPlatformExceptionHandler {

    @ExceptionHandler(value = {
            IncorrectWebhookModeException.class,
            IncorrectTokenException.class
    })
    public ResponseEntity<?> handleWebhookVerificationExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
