package com.xalpol12.messengerbot.crud.exception.utils;

import com.xalpol12.messengerbot.crud.exception.customexception.ImageAccessException;
import com.xalpol12.messengerbot.crud.exception.customexception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.exception.customexception.ScheduledMessageNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ExceptionUtils {

    private static final Map<Class<? extends Exception>, String> ERROR_CODE_MAP = new HashMap<>();

    static {
        ERROR_CODE_MAP.put(ImageAccessException.class, "IMAGE_ACCESS_EXCEPTION");
        ERROR_CODE_MAP.put(IllegalArgumentException.class, "IMAGE_DIMENSIONS_ACCESS_ERROR");
        ERROR_CODE_MAP.put(ImageNotFoundException.class, "IMAGE_NOT_FOUND");
        ERROR_CODE_MAP.put(ScheduledMessageNotFoundException.class, "SCHEDULED_MESSAGE_NOT_FOUND");
        ERROR_CODE_MAP.put(MethodArgumentNotValidException.class, "METHOD_ARGUMENT_NOT_VALID");
        ERROR_CODE_MAP.put(ConstraintViolationException.class, "CONSTRAINT_VIOLATED");
    }

    public static String getErrorCode(Exception ex) {
        return ERROR_CODE_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(ex))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("UNKNOWN_ERROR_CODE");
    }

    public static String getMessageAndLog(RuntimeException e) {
        String message = e.getMessage();
        log.error("Encountered {} with message: {}", e.getClass().getName(), message);
        return message;
    }
}
