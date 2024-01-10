package com.xalpol12.messengerbot.crud.exception;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CrudExceptionHandlerTest {

    private static CrudExceptionHandler exceptionHandler;

    @BeforeAll
    public static void setup() {
        exceptionHandler = new CrudExceptionHandler();
    }

    @Test
    public void handleImageAccessException() {
        String message = "Exception message";
        ImageAccessException exception = new ImageAccessException(message);

        ResponseEntity<String> response = exceptionHandler.handleImageAccessException(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }

    @Test
    public void handleImageNotFoundException() {
        String message = "Exception message";
        ImageNotFoundException exception = new ImageNotFoundException(message);

        ResponseEntity<String> response = exceptionHandler.handleImageNotFoundException(exception);

        assertAll(() -> {
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(message, response.getBody());
        });
    }

    @Test
    public void handlePSQLException_uniqueConstraintExceptionViolated() throws PSQLException {
        PSQLState uniqueViolation = PSQLState.UNIQUE_VIOLATION;
        PSQLException exception = new PSQLException("Message", uniqueViolation);

        ResponseEntity<String> response = exceptionHandler.handlePSQLException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void handlePSQLException_otherErrorCodeEncountered() {
        PSQLState uniqueViolation = PSQLState.IO_ERROR;
        PSQLException exception = new PSQLException("Message", uniqueViolation);

        assertThrows(PSQLException.class, () -> exceptionHandler.handlePSQLException(exception));
    }
}