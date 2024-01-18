package com.xalpol12.messengerbot.crud.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception handler for CRUD module. Returns response entities
 * with String message body and corresponding HttpStatus code
 */
@Slf4j
@RestControllerAdvice
public class CrudExceptionHandler {

    /**
     * Raised when service couldn't access byte data
     * in provided MultipartFile.
     * @param e ImageAccessException
     * @return ResponseEntity<String> with exception details
     * and error code 400
     */
    @ExceptionHandler(value = {
            ImageAccessException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<String> handleImageAccessException(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Raised when service couldn't find any entity
     * with given ID in a database.
     * @param e RuntimeException
     * @return ResponseEntity<String> with exception details
     * and error code 404
     */
    @ExceptionHandler(value = {
            ImageNotFoundException.class,
            ScheduledMessageNotFoundException.class
    })
    public ResponseEntity<String> handleImageNotFoundException(RuntimeException e) {
        String message = extractMessageAndLog(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Exception raised when PostgreSQL
     * encounters an error.
     * @param e PSQLException with specified SQLState.
     * @return ResponseEntity<String> with a status code
     * and error message specifying the details of
     * caught exception.
     * @throws PSQLException rethrows exception
     * if PSQLException contains SQLState that is not
     * currently handled by the method.
     */
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

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            NumberFormatException.class
    })
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private String extractMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
