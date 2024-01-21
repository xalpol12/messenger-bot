package com.xalpol12.messengerbot.messengerplatform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.IncorrectTokenException;
import com.xalpol12.messengerbot.messengerplatform.exception.customexception.IncorrectWebhookModeException;
import com.xalpol12.messengerbot.publisher.service.SubscriberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebhookServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SecretsConfig secretsConfig;

    @Mock
    private SubscriberService subscriberService;

    private WebhookService webhookService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        webhookService = new WebhookService(objectMapper, secretsConfig, subscriberService);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    @Disabled
    // TODO: Fix
    public void process_shouldCallSubscriberService() {
        String payload = getPayload();

        webhookService.process(payload);

        verify(subscriberService, times(1)).addNewSubscribers(Set.of("2"));
    }

    @Test
    // TODO: Fix
    public void process_shouldThrowNewIncorrectWebhookObjectTypeException() {

    }

    @Test
    public void verifyWebhookSubscription_shouldReturnChallenge() {
        String mode = "subscribe";
        String token = "token";
        String challenge = "challenge";

        when(secretsConfig.getVerificationToken()).thenReturn(token);

        String result = webhookService.verifyWebhookSubscription(mode, token, challenge);

        assertEquals(challenge, result);
    }


    @Test
    public void verifyWebhookSubscription_shouldThrowIncorrectTokenException() {
        String mode = "subscribe";
        String token = "incorrect-token";
        String challenge = "challenge";

        when(secretsConfig.getVerificationToken()).thenReturn("token");

        assertThrows(IncorrectTokenException.class,
                () -> webhookService.verifyWebhookSubscription(mode, token, challenge));
    }

    @Test
    public void verifyWebhookSubscription_shouldThrowIncorrectWebhookModeException() {
        String mode = "not-subscribe";
        String token = "token";
        String challenge = "challenge";

        assertThrows(IncorrectWebhookModeException.class,
                () -> webhookService.verifyWebhookSubscription(mode, token, challenge));
    }

    @Disabled
    @Test //TODO: Fix signature verification
    public void verifyRequestSignature_shouldPass() {
        String secret = "secret-key";
        String signature = "sha256=546af7454d67e6ca50e40790a32483c22edac825792a044a21b01913ee1e5d2a"; // precomputed expected signature that
        // should match the server computation
        String payload = """
                {"object":"page","entry":[{"id":"<PAGE_ID>","time":1458692752478,"messaging":[{"sender":{"id":"<PSID>"},"recipient":{"id":"<PAGE_ID>"}}]}]}
                """; // payload which will be used to compute signature

        when(secretsConfig.getSecretKey()).thenReturn(secret);

        // it matches if no exception is thrown
        assertDoesNotThrow(() -> webhookService.verifyRequestSignature(signature, payload));
    }

    private String getPayload() {
        return """
                  {
                  "object": "page",
                  "entry": [
                    {
                      "id": "1",
                      "time": 1703939021079,
                      "messaging": [
                        {
                          "sender": {
                            "id": "20"
                          },
                          "recipient": {
                            "id": "21"
                          },
                          "timestamp": 1703939020568,
                          "message": {
                            "mid": "ffzTGhLub6dL-0hYiKzGaMwI3qiOgSARWH_1LRdTf102gqLW2g",
                            "text": "witam serdecznie"
                          }
                        }
                      ]
                    }
                  ]
                }""";
    }
}