package com.xalpol12.messengerbot.crud.exception;

/**
 * Exception that signals a problem with
 * Image entity access in the database.
 */
public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super();
    }

    public ImageNotFoundException(String message) {
        super(message);
    }
}
