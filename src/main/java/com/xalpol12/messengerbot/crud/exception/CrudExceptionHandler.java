package com.xalpol12.messengerbot.crud.exception;

import com.xalpol12.messengerbot.crud.exception.customexception.ImageAccessException;
import com.xalpol12.messengerbot.crud.exception.customexception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.exception.customexception.ScheduledMessageNotFoundException;
import com.xalpol12.messengerbot.core.exception.response.CustomErrorResponse;
import com.xalpol12.messengerbot.core.exception.utils.ExceptionUtils;
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
 * with CustomerErrorResponse body and corresponding HttpStatus code
 */
@Slf4j
@RestControllerAdvice
public class CrudExceptionHandler {

    /**
     * Raised when service couldn't access byte data
     * in provided MultipartFile.
     * @param e ImageAccessException
     * @return ResponseEntity with CustomErrorResponse
     * body and exception details with error code 400
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
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 404
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
     * @return ResponseEntity containing CustomErrorResponse
     * with a status code and error message
     * specifying the details of caught exception.
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

    /**
     * Raised when provided request with
     * invalid content.
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionUtils.getErrorCode(e);
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, fieldErrors);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Raised when one or more entity constraints
     * have been violated.
     * @param e ConstraintViolationException
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolationExceptions(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionUtils.getErrorCode(e);
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        }
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, fieldErrors);
        return new ResponseEntity<>(response, status);
    }
}
