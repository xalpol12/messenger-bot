package com.xalpol12.messengerbot.messengerplatform.service;

import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectTokenException;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectWebhookModeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    @Value("${messenger.app.verification.token}")
    private final String verificationToken;

    public void process(String webhookPayload) {
        // check if body.object === "page" like in js example - whatever that means
        // or throw new exception to send 404 not found if else
        log.info("Processed webhook with content: {}", webhookPayload);
    }

    public String verifyWebhook(String mode, String token, String challenge) {
        if (mode.equals("subscribe") ) {
            if (token.equals(verificationToken)) {
                return challenge;
            } else throw new IncorrectTokenException("Received token does not match the expected token");
        } else throw new IncorrectWebhookModeException("Webhook mode is not set to 'subscribe'");
    }
}
