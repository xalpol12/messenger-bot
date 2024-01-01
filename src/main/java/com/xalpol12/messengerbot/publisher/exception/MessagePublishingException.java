package com.xalpol12.messengerbot.publisher.exception;

public class MessagePublishingException extends RuntimeException {
    public MessagePublishingException() {
        super();
    }

    public MessagePublishingException(String message) {
        super(message);
    }
}
