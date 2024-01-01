package com.xalpol12.messengerbot.publisher.exception;

public class MessagePublishingException extends RuntimeException { //TODO: Handle this exception in some smart way to
    // signal administrator on the frontend that something went wrong
    public MessagePublishingException() {
        super();
    }

    public MessagePublishingException(String message) {
        super(message);
    }
}
