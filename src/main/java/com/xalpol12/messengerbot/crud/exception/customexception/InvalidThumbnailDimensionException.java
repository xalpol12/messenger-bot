package com.xalpol12.messengerbot.crud.exception.customexception;

/**
 * Exception that signals a problem with requested
 * thumbnail dimensions.
 */
public class InvalidThumbnailDimensionException extends RuntimeException {
    public InvalidThumbnailDimensionException(String message) {
        super(message);
    }
}
