package com.xalpol12.messengerbot.messengerplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.crud.controller.ScheduledMessageController;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import com.xalpol12.messengerbot.messengerplatform.service.WebhookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebhookService webhookService;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void receiveWebhook_expect200() throws Exception {
        String signature = "signature";
        String payload = "example-payload";
        String endpointPath = WebhookController.WebhookPath.ROOT;
        String expected = "EVENT_RECEIVED";

        this.mockMvc
                .perform(post(endpointPath)
                        .content(payload)
                        .header("x-hub-signature-256", signature)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));

        verify(webhookService, times(1)).process(payload);
    }

    @Test
    public void verifyWebhookSubscription_expect200() throws Exception {
        String mode = "subscription";
        String token = "token";
        String challenge = "challenge";
        String endpointPath = WebhookController.WebhookPath.ROOT;

        when(webhookService.verifyWebhookSubscription(mode, token, challenge)).thenReturn(challenge);

        this.mockMvc
                .perform(get(endpointPath)
                        .param("hub.mode", mode)
                        .param("hub.verify_token", token)
                        .param("hub.challenge", challenge))
                .andExpect(status().isOk())
                .andExpect(content().string(challenge));

        verify(webhookService, times(1)).verifyWebhookSubscription(mode, token, challenge);
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}