package com.xalpol12.messengerbot.messengerplatform.exception;

public class IncorrectTokenException extends RuntimeException {
    public IncorrectTokenException() {
        super();
    }

    public IncorrectTokenException(String message) {
        super(message);
    }
}
