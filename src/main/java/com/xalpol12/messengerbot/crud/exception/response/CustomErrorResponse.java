package com.xalpol12.messengerbot.crud.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

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

    public static CustomErrorResponse create(HttpStatus status, String error, String message) {
        CustomErrorResponse response = createDefaultCustomErrorResponse(status, error);
        response.setMessage(message);
        return response;
    }

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
