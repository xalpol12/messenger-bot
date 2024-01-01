package com.xalpol12.messengerbot.crud.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CrudExceptionHandler {

    @ExceptionHandler(ImageAccessException.class)
    public ResponseEntity<String> handleImageAccessException(ImageAccessException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            ImageNotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<String> handleImageNotFoundException(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> handlePSQLException(PSQLException e) throws PSQLException {
        if (e.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
            String errorMessage = "Provided custom uri that already exists in a database";
            log.error("Encountered {} with message: {}", e.getClass().getName(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } else {
            throw e;
        }
    }

    private String extractMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
