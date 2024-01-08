package com.xalpol12.messengerbot.publisher.exception;

/**
 * Exception occurs when HTTP client couldn't
 * send a call to Facebook Graph API successfully.
 */
public class MessagePublishingException extends RuntimeException { //TODO: Handle this exception in some smart way to
    // signal administrator on the frontend that something went wrong

    public MessagePublishingException(String message) {
        super(message);
    }
}
