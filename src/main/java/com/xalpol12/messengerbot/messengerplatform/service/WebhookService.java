package com.xalpol12.messengerbot.messengerplatform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectTokenException;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectWebhookModeException;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectWebhookObjectTypeException;
import com.xalpol12.messengerbot.messengerplatform.exception.RequestSignatureValidationException;
import com.xalpol12.messengerbot.messengerplatform.model.Webhook;
import com.xalpol12.messengerbot.messengerplatform.model.composite.ChatEntry;
import com.xalpol12.messengerbot.messengerplatform.model.composite.WebhookEntry;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import com.xalpol12.messengerbot.publisher.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Methods for receiving and serving webhook incoming calls.
 * Constructed in accordance with Messenger Platform API documentation.
 * @see <a href="https://developers.facebook.com/docs/messenger-platform/webhooks">Messenger Platform API link</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final ObjectMapper objectMapper;

    private final SecretsConfig secrets;
    private final SubscriberService subscriberService;

    private static final String HASHING_ALGORITHM = "HmacSHA256";

    /**
     * Currently this method accepts webhooks sent when
     * someone messages a fanpage.
     * It processes all sender ids and adds new ids
     * to the database.
     * @param webhookPayload - Webhook content
     * @throws IncorrectWebhookObjectTypeException if received webhook
     * was not of type "page"
     */
    public void process(String webhookPayload) {
        try {
            Webhook webhook = objectMapper.readValue(webhookPayload, Webhook.class);
            if (webhook.getObject().equals("page")) {
                Set<String> senderIds = extractSenderIds(webhook);
                subscriberService.addNewSubscribers(senderIds);
            } else {
                throw new IncorrectWebhookObjectTypeException("Received webhook is not of type \"page\"");
            }
        } catch (Exception e) {
            log.error("Couldn't process webhook with unknown structure: {}", webhookPayload);
        }
    }

    private Set<String> extractSenderIds(Webhook webhook) {
        List<List<ChatEntry>> chatEntries = webhook.getEntry()
                .stream()
                .map(WebhookEntry::getMessaging)
                .toList();

        return chatEntries.stream()
                .flatMap(List::stream)
                .map(ChatEntry::getSender)
                .map(Subject::getId)
                .collect(Collectors.toSet());
    }

    /**
     * Checks if mode and token are of the expected values.
     * In case of successful validation returns challenge.
     * @param mode Expected "subscribe" value
     * @param token Secret verification token that only service and
     *              Messenger Platform API know
     * @param challenge Return value in case of successful verification
     * @return challenge
     * @throws IncorrectTokenException if received token does not
     * match the expected token
     * @throws IncorrectWebhookModeException if mode value was not 'subscribe'
     */
    public String verifyWebhookSubscription(String mode, String token, String challenge) {
        if (mode.equals("subscribe") ) {
            if (token.equals(secrets.getVerificationToken())) {
                return challenge;
            } else throw new IncorrectTokenException("Received token does not match the expected token.");
        } else throw new IncorrectWebhookModeException("Webhook mode is not set to 'subscribe'.");
    }

    /**
     * Calculates the checksum and verifies if received checksum
     * matches the calculated checksum.
     * @param receivedSignature Checksum calculated by Messenger Platform API
     * @param payload Webhook notification request body
     * @throws RequestSignatureValidationException if validation was not successful
     */
    public void verifyRequestSignature(String receivedSignature, String payload) {
        String signatureHash = receivedSignature.split("=")[1];
        try {
            String expectedHash = generateHmacSha256(payload, secrets.getSecretKey());
            if (!signatureHash.equals(expectedHash)) {
                throw new RequestSignatureValidationException("Couldn't validate the request signature");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RequestSignatureValidationException("Couldn't validate the request signature. " + e.getMessage());
        }
    }

    private String generateHmacSha256(String input, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance(HASHING_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HASHING_ALGORITHM);
        sha256Hmac.init(secretKey);
        byte[] hashBytes = sha256Hmac.doFinal(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
