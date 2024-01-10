package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.messengerplatform.config.secrets.SecretsConfig;
import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageParams;
import com.xalpol12.messengerbot.publisher.client.MessengerAPIClient;
import com.xalpol12.messengerbot.publisher.config.FacebookVariables;
import com.xalpol12.messengerbot.publisher.exception.MessagePublishingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacebookPageAPIServiceTest {

    @Mock
    private FacebookVariables fbVars;
    @Mock
    private SecretsConfig secretsConfig;
    @Mock
    private MessengerAPIClient messengerClient;

    private FacebookPageAPIService facebookPageAPIService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        facebookPageAPIService = new FacebookPageAPIService(fbVars, secretsConfig, messengerClient);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void sendMessage_shouldInvokeMessengerClient() throws MessagePublishingException, IOException {
        String userId = "userId";
        String message = "message";
        String secretKey = "secret-key";
        String apiVersion = "v18.0";
        String pageId = "page-id";

        when(secretsConfig.getSecretKey()).thenReturn(secretKey);
        when(fbVars.getApiVersion()).thenReturn(apiVersion);
        when(fbVars.getPageId()).thenReturn(pageId);

        facebookPageAPIService.sendMessage(userId, message);

        verify(messengerClient).sendMessage(eq(apiVersion), eq(pageId), any(MessageParams.class));
    }

    @Test
    public void sendMessage_shouldThrowMessagePublishingException() throws IOException {

        String userId = "userId";
        String message = "message";
        String secretKey = "secret-key";
        String apiVersion = "v18.0";
        String pageId = "page-id";

        when(secretsConfig.getSecretKey()).thenReturn(secretKey);
        when(fbVars.getApiVersion()).thenReturn(apiVersion);
        when(fbVars.getPageId()).thenReturn(pageId);
        doThrow(new IOException())
                .when(messengerClient)
                .sendMessage(eq(apiVersion), eq(pageId), any(MessageParams.class));

        assertThrows(MessagePublishingException.class, () -> facebookPageAPIService.sendMessage(userId, message));
    }
}