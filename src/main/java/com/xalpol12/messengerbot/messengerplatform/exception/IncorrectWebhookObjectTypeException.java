package com.xalpol12.messengerbot.messengerplatform.exception;

public class IncorrectWebhookObjectTypeException extends RuntimeException {
    public IncorrectWebhookObjectTypeException() {
        super();
    }

    public IncorrectWebhookObjectTypeException(String message) {
        super(message);
    }
}
