package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Message;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import com.xalpol12.messengerbot.publisher.client.MessengerAPIClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacebookPageAPIService {
    private final String MESSAGING_TYPE = "RESPONSE";

    @Value("${facebook.api.version}")
    private String apiVersion;

    @Value("${facebook.page.id}")
    private String pageId; //TODO: add support for multiple facebook pages

    private final SecretsConfig secretsConfig;
    private final MessengerAPIClient messengerClient;

    public void sendMessage(String userId, ScheduledMessage scheduledMessage) {
        Subject recipient = new Subject(userId);
        Message message = new Message(scheduledMessage.getMessage());
        String accessToken = secretsConfig.getSecretKey();

        messengerClient.sendMessage(apiVersion, pageId, recipient, message, MESSAGING_TYPE, accessToken);
        log.info("User: {} received message: {}", userId, message);
    }
}
