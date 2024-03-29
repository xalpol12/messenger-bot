package com.xalpol12.messengerbot.messengerplatform.exception.customexception;

/**
 * Exception occurs when received checksum
 * doesn't match the calculated checksum.
 */
public class RequestSignatureValidationException extends RuntimeException {

    public RequestSignatureValidationException(String message) {
        super(message);
    }
}
