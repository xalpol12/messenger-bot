package com.xalpol12.messengerbot.crud.exception.utils;

import com.xalpol12.messengerbot.crud.exception.customexception.ImageAccessException;
import com.xalpol12.messengerbot.crud.exception.customexception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.exception.customexception.ScheduledMessageNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
public class ExceptionUtils {

    public static String getErrorCode(Exception ex) {
        if (ex instanceof ImageAccessException) {
            return "IMAGE_ACCESS_EXCEPTION";
        } else if (ex instanceof IllegalArgumentException) {
            return "IMAGE_DIMENSIONS_ACCESS_ERROR";
        } else if (ex instanceof ImageNotFoundException) {
            return "IMAGE_NOT_FOUND";
        } else if (ex instanceof ScheduledMessageNotFoundException) {
            return "SCHEDULED_MESSAGE_NOT_FOUND";
        } else if (ex instanceof MethodArgumentNotValidException) {
            return "METHOD_ARGUMENT_NOT_VALID";
        } else if (ex instanceof ConstraintViolationException) {
            return "CONSTRAINT_VIOLATED";
        };
        return "UNKNOWN_ERROR_CODE";
    };

    public static String getMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
