package com.xalpol12.messengerbot.messengerplatform.exception;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessengerPlatformExceptionHandlerTest {

    private static MessengerPlatformExceptionHandler exceptionHandler;

    @BeforeAll
    public static void setup() {
        exceptionHandler = new MessengerPlatformExceptionHandler();
    }

    @Test
    public void handleWebhookVerificationExceptions_incorrectWebhookModeExceptionEncountered() {
        String message = "Exception message";
        IncorrectWebhookModeException exception = new IncorrectWebhookModeException(message);

        ResponseEntity<String> response = exceptionHandler.handleWebhookVerificationExceptions(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }

    @Test
    public void handleWebhookVerificationExceptions_incorrectWebhookTokenExceptionEncountered() {
        String message = "Exception message";
        IncorrectTokenException exception = new IncorrectTokenException(message);

        ResponseEntity<String> response = exceptionHandler.handleWebhookVerificationExceptions(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }

    @Test
    public void handleHashValidationException_requestSignatureValidationEncountered() {
        String message = "Exception message";
        RequestSignatureValidationException exception = new RequestSignatureValidationException(message);

        ResponseEntity<String> response = exceptionHandler.handleHashValidationException(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }

    @Test
    public void handleIncorrectWebhookObjectType_incorrectWebhookObjectTypeExceptionEncountered() {
        String message = "Exception message";
        IncorrectWebhookObjectTypeException exception = new IncorrectWebhookObjectTypeException(message);

        ResponseEntity<String> response = exceptionHandler.handleIncorrectWebhookObjectType(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }
}