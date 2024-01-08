package com.xalpol12.messengerbot.messengerplatform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for Messenger Platform module.
 * Returns response entities with String message body
 * and corresponding HttpStatus code.
 */
@Slf4j
@RestControllerAdvice
public class MessengerPlatformExceptionHandler {

    /**
     * Raised when service couldn't correctly verify
     * the incoming webhook request.
      * @param e RuntimeException
     * @return ResponseEntity<String> with exception details
     * and error code 403
     */
    @ExceptionHandler(value = {
            IncorrectWebhookModeException.class,
            IncorrectTokenException.class
    })
    public ResponseEntity<String> handleWebhookVerificationExceptions(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    /**
     * Raised when request signature validation failed.
     * @param e RequestSignatureValidationException
     * @return ResponseEntity<String> with exception details
     * and error code 400
     */
    @ExceptionHandler(value = {RequestSignatureValidationException.class})
    public ResponseEntity<String> handleHashValidationException(RequestSignatureValidationException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Raised when service received unexpected webhook object structure.
     * @param e IncorrectWebhookObjectTypeException
     * @return ResponseEntity<String> with exception details
     * and error code 404
     */
    @ExceptionHandler(value = {IncorrectWebhookObjectTypeException.class})
    public ResponseEntity<String> handleIncorrectWebhookObjectType(IncorrectWebhookObjectTypeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    private String extractMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
