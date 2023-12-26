package com.xalpol12.messengerbot.messengerplatform.exception;

public class IncorrectWebhookModeException extends RuntimeException {
    public IncorrectWebhookModeException() {
        super();
    }

    public IncorrectWebhookModeException(String message) {
        super(message);
    }
}
