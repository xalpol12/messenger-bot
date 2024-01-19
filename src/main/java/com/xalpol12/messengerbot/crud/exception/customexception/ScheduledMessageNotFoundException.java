package com.xalpol12.messengerbot.crud.exception.customexception;

/**
 * Exception that signals a problem with
 * ScheduledMessage entity access in the database.
 */
public class ScheduledMessageNotFoundException extends RuntimeException {

    public ScheduledMessageNotFoundException(String message) {
        super(message);
    }
}
