package com.xalpol12.messengerbot.messengerplatform.exception.customexception;

/**
 * Exception thrown when service receives
 * incorrect token.
 */
public class IncorrectTokenException extends RuntimeException {

    public IncorrectTokenException(String message) {
        super(message);
    }
}
