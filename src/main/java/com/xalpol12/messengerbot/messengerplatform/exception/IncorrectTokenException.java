package com.xalpol12.messengerbot.messengerplatform.exception;

/**
 * Exception thrown when service receives
 * incorrect token.
 */
public class IncorrectTokenException extends RuntimeException {
    public IncorrectTokenException() {
        super();
    }

    public IncorrectTokenException(String message) {
        super(message);
    }
}
