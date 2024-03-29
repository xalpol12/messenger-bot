package com.xalpol12.messengerbot.messengerplatform.exception.customexception;

/**
 * Exception occurs when service received unexpected
 * webhook structure and object couldn't be deserialized
 */
public class IncorrectWebhookObjectTypeException extends RuntimeException {

    public IncorrectWebhookObjectTypeException(String message) {
        super(message);
    }
}
