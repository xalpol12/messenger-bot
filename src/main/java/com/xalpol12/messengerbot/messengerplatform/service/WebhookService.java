package com.xalpol12.messengerbot.messengerplatform.service;

import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectTokenException;
import com.xalpol12.messengerbot.messengerplatform.exception.IncorrectWebhookModeException;
import com.xalpol12.messengerbot.messengerplatform.exception.RequestSignatureValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final SecretsConfig secrets;

    private static final String HASHING_ALGORITHM = "HmacSHA256";


    public void process(String webhookPayload) {
        // check if body.object === "page" like in js example - whatever that means
        // or throw new exception to send 404 not found if else
        log.info("Processed webhook with content: {}", webhookPayload);
    }

    public String verifyWebhookSubscription(String mode, String token, String challenge) {
        if (mode.equals("subscribe") ) {
            if (token.equals(secrets.getVerificationToken())) {
                return challenge;
            } else throw new IncorrectTokenException("Received token does not match the expected token.");
        } else throw new IncorrectWebhookModeException("Webhook mode is not set to 'subscribe'.");
    }

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
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
