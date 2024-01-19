package com.xalpol12.messengerbot.crud.exception;

import com.xalpol12.messengerbot.crud.exception.customexception.ImageAccessException;
import com.xalpol12.messengerbot.crud.exception.customexception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.exception.customexception.ScheduledMessageNotFoundException;
import com.xalpol12.messengerbot.crud.exception.response.CustomErrorResponse;
import com.xalpol12.messengerbot.crud.exception.utils.ExceptionUtils;
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
    public ResponseEntity<CustomErrorResponse> handleImageAccessException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = ExceptionUtils.getErrorCode(e);
        String message = ExceptionUtils.getMessageAndLog(e);
        CustomErrorResponse response = CustomErrorResponse.create(status, error, message);
        return new ResponseEntity<>(response, status);
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
    public ResponseEntity<CustomErrorResponse> handleImageNotFoundException(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String error = ExceptionUtils.getErrorCode(e);
        String message = ExceptionUtils.getMessageAndLog(e);
        CustomErrorResponse response = CustomErrorResponse.create(status, error, message);
        return new ResponseEntity<>(response, status);
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
    public ResponseEntity<CustomErrorResponse> handlePSQLException(PSQLException e) throws PSQLException {
        String psqlState = e.getSQLState();
        if (psqlState.equals(PSQLState.UNIQUE_VIOLATION.getState())) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String error = PSQLState.UNIQUE_VIOLATION.toString();
            String message = "Provided custom uri that already exists in a database";
            CustomErrorResponse response = CustomErrorResponse.create(status, error, message);
            log.error("Encountered {} with message: {}", e.getClass().getName(), message);
            return new ResponseEntity<>(response, status);
        } else {
            throw e;
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionUtils.getErrorCode(ex);
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, fieldErrors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionUtils.getErrorCode(ex);
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        }
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, fieldErrors);
        return new ResponseEntity<>(response, status);
    }
}
