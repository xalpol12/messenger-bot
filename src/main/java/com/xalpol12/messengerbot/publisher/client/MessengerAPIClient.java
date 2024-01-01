package com.xalpol12.messengerbot.publisher.client;

import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageParams;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MessengerAPIClient {

    private final OkHttpClient okHttpClient;
    private final String BASE_URL = "https://graph.facebook.com";
    private final String MESSAGES_ENDPOINT = "/messages";

    public void sendMessage(String apiVersion, String clientId, MessageParams messageParams)
            throws IOException {

        String fullUrl = formatUrl(apiVersion, clientId) + MESSAGES_ENDPOINT;

        String recipient = String.format("{id:%s}", messageParams.recipient());
        String message = String.format("{text:'%s'}", messageParams.message());

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(fullUrl)).newBuilder();
        urlBuilder.addQueryParameter("recipient", recipient);
        urlBuilder.addQueryParameter("message", message);
        urlBuilder.addQueryParameter("messaging_type", messageParams.messagingType());
        urlBuilder.addQueryParameter("access_token", messageParams.accessToken());

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(RequestBody.create("", null))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    private String formatUrl(String apiVersion, String clientId) {
        return String.format("%s/%s/%s", BASE_URL, apiVersion, clientId);
    }
}
