package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageParams;
import com.xalpol12.messengerbot.publisher.client.MessengerAPIClient;
import com.xalpol12.messengerbot.publisher.exception.MessagePublishingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacebookPageAPIService {

    private final String MESSAGING_TYPE = "RESPONSE";

    @Value("${facebook.api.version}")
    private String API_VERSION;

    @Value("${facebook.page.id}")
    private String PAGE_ID; //TODO: add support for multiple facebook pages

    private final SecretsConfig secretsConfig;
    private final MessengerAPIClient messengerClient;

    public void sendMessage(String userId, String message) {
        MessageParams params = MessageParams.builder()
                .recipient(userId)
                .message(message)
                .messagingType(MESSAGING_TYPE)
                .accessToken(secretsConfig.getSecretKey())
                .build();

        try {
            messengerClient.sendMessage(API_VERSION, PAGE_ID, params);
            log.info("User: {} received message: {}", userId, message);
        } catch (IOException e) {
            throw new MessagePublishingException("Could not publish message to user with id: "
                    + userId + ". Message status code: " + e.getMessage());
        }
    }
}
