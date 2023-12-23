package com.xalpol12.messengerbot.crud.exception;

public class ImageAccessException extends RuntimeException {

    public ImageAccessException() {
        super();
    }

    public ImageAccessException(String details) {
        super(details);
    }
}
