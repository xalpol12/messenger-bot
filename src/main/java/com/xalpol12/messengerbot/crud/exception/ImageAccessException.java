package com.xalpol12.messengerbot.crud.exception;

/**
 * Exception that signals a problem with
 * data access in provided image MultipartFile.
 */
public class ImageAccessException extends RuntimeException {

    public ImageAccessException() {
        super();
    }

    public ImageAccessException(String details) {
        super(details);
    }
}
