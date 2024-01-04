package com.xalpol12.messengerbot.crud.exception;

public class ScheduledMessageNotFoundException extends RuntimeException {
    public ScheduledMessageNotFoundException() {
        super();
    }

    public ScheduledMessageNotFoundException(String message) {
        super(message);
    }
}
