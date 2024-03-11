package com.xalpol12.messengerbot.messengerplatform.exception;

import com.xalpol12.messengerbot.core.exception.response.CustomErrorResponse;
import com.xalpol12.messengerbot.core.exception.utils.ExceptionUtils;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.IncorrectTokenException;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.IncorrectWebhookModeException;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.IncorrectWebhookObjectTypeException;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.RequestSignatureValidationException;
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
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 403
     */
    @ExceptionHandler(value = {
            IncorrectWebhookModeException.class,
            IncorrectTokenException.class
    })
    public ResponseEntity<CustomErrorResponse> handleWebhookVerificationExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String errorCode = ExceptionUtils.getErrorCode(e);
        String message = ExceptionUtils.getMessageAndLog(e);
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, message);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Raised when request signature validation failed.
     * @param e RequestSignatureValidationException
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 400
     */
    @ExceptionHandler(value = {RequestSignatureValidationException.class})
    public ResponseEntity<CustomErrorResponse> handleHashValidationException(RequestSignatureValidationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = ExceptionUtils.getErrorCode(e);
        String message = ExceptionUtils.getMessageAndLog(e);
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, message);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Raised when service received unexpected webhook object structure.
     * @param e IncorrectWebhookObjectTypeException
     * @return ResponseEntity with CustomErrorResponse and exception details
     * with error code 404
     */
    @ExceptionHandler(value = {IncorrectWebhookObjectTypeException.class})
    public ResponseEntity<CustomErrorResponse> handleIncorrectWebhookObjectType(IncorrectWebhookObjectTypeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorCode = ExceptionUtils.getErrorCode(e);
        String message = ExceptionUtils.getMessageAndLog(e);
        CustomErrorResponse response = CustomErrorResponse.create(status, errorCode, message);
        return new ResponseEntity<>(response, status);
    }
}
