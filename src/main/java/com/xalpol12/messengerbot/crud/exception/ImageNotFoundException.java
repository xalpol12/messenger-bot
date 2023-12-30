package com.xalpol12.messengerbot.crud.exception;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super();
    }

    public ImageNotFoundException(String message) {
        super(message);
    }
}
