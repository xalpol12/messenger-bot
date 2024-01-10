package com.xalpol12.messengerbot.publisher.exception;

/**
 * Exception occurs when HTTP client couldn't
 * send a call to Facebook Graph API successfully.
 */
public class MessagePublishingException extends Exception {

    public MessagePublishingException(String message) {
        super(message);
    }
}
