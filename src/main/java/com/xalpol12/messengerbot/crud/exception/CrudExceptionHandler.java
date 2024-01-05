package com.xalpol12.messengerbot.crud.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    @ExceptionHandler(ImageAccessException.class)
    public ResponseEntity<String> handleImageAccessException(ImageAccessException e) {
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

    private String extractMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
