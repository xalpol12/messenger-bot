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

    public Response sendMessage(String apiVersion, String clientId, MessageParams messageParams)
            throws IOException {
        String fullUrl = formatUrl(apiVersion, clientId) + MESSAGES_ENDPOINT;
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(fullUrl)).newBuilder();
        urlBuilder.addQueryParameter("recipient", messageParams.recipient().getId());
        urlBuilder.addQueryParameter("message", messageParams.message().getText());
        urlBuilder.addQueryParameter("messaging_type", messageParams.messagingType());
        urlBuilder.addQueryParameter("access_token", messageParams.accessToken());

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(RequestBody.create("", null))
                .build();

        return okHttpClient.newCall(request).execute();
    }

    private String formatUrl(String apiVersion, String clientId) {
        return String.format("%s/%s/%s", BASE_URL, apiVersion, clientId);
    }
}
