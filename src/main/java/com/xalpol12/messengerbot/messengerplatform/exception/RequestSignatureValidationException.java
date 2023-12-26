package com.xalpol12.messengerbot.messengerplatform.exception;

public class RequestSignatureValidationException extends RuntimeException {
    public RequestSignatureValidationException() {
        super();
    }

    public RequestSignatureValidationException(String message) {
        super(message);
    }
}
