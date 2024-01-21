package com.xalpol12.messengerbot.messengerplatform.exception.customexception;

/**
 * Exception occurs when received webhook mode is not the
 * expected webhook mode.
 */
public class IncorrectWebhookModeException extends RuntimeException {

    public IncorrectWebhookModeException(String message) {
        super(message);
    }
}
