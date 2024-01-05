package com.xalpol12.messengerbot.messengerplatform.exception;

/**
 * Exception occurs when received checksum
 * doesn't match the calculated checksum.
 */
public class RequestSignatureValidationException extends RuntimeException {
    public RequestSignatureValidationException() {
        super();
    }

    public RequestSignatureValidationException(String message) {
        super(message);
    }
}
