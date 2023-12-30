package com.xalpol12.messengerbot.messengerplatform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MessengerPlatformExceptionHandler {

    @ExceptionHandler(value = {
            IncorrectWebhookModeException.class,
            IncorrectTokenException.class
    })
    public ResponseEntity<?> handleWebhookVerificationExceptions(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {RequestSignatureValidationException.class})
    public ResponseEntity<?> handleHashValidationException(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    private String extractMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
