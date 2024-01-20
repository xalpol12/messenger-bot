package com.xalpol12.messengerbot.crud.exception.customexception;

/**
 * Exception that signals a problem with
 * Image entity access in the database.
 */
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String message) {
        super(message);
    }
}