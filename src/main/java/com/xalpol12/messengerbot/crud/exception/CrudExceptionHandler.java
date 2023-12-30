package com.xalpol12.messengerbot.crud.exception;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CrudExceptionHandler {

    @ExceptionHandler(ImageAccessException.class)
    public ResponseEntity<String> handleImageAccessException(ImageAccessException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handleImageNotFoundException(ImageNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> handlePSQLException(PSQLException e) throws PSQLException {
        if (e.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
            String errorMessage = "Provided custom uri that already exists in a database";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } else {
            throw e;
        }
    }
}
