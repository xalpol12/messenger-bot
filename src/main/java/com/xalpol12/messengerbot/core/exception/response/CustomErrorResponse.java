package com.xalpol12.messengerbot.core.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Custom error message format to send in ResponseEntity when
 * ExceptionController catches exception.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;

    /**
     * Factory method for creating new CustomErrorResponse instance
     * from HttpStatus, error String and message String.
     * @param status HttpStatus enumerator
     * @param error String with predefined error code name
     * @param message readable message for clarifying the cause of encountered error
     * @return new CustomErrorResponse instance
     */
    public static CustomErrorResponse create(HttpStatus status, String error, String message) {
        CustomErrorResponse response = createDefaultCustomErrorResponse(status, error);
        response.setMessage(message);
        return response;
    }

    /**
     * Factory method for creating new CustomErrorResponse instance
     * from HttpStatus, error String and fieldErrors map.
     * @param status HttpStatus enumerator
     * @param error String with predefined error code name
     * @param fieldErrors Map<String, String> with all field errors
     * @return new CustomErrorResponse instance
     */
    public static CustomErrorResponse create(HttpStatus status, String error, Map<String, String> fieldErrors) {
        CustomErrorResponse response = createDefaultCustomErrorResponse(status, error);
        response.setFieldErrors(fieldErrors);
        return response;
    }

    private static CustomErrorResponse createDefaultCustomErrorResponse(HttpStatus status, String error) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status.value());
        response.setError(error);
        return response;
    }
}
