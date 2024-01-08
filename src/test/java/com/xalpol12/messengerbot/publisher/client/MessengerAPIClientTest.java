package com.xalpol12.messengerbot.publisher.client;

import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageParams;
import okhttp3.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessengerAPIClientTest {

    @Mock
    private OkHttpClient okHttpClient;

    @InjectMocks
    private MessengerAPIClient messengerAPIClient;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        messengerAPIClient = new MessengerAPIClient(okHttpClient);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void sendMessage_shouldCallSuccessfully() throws IOException {
        String apiVersion = "v1.0";
        String clientId = "client-id";
        MessageParams messageParams = MessageParams.builder().build();

        // mocking okhttp:
        Call call = mock(Call.class);
        Request request = mock(Request.class);
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build();

        when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        assertDoesNotThrow(() -> messengerAPIClient.sendMessage(apiVersion, clientId, messageParams));
        verify(okHttpClient).newCall(any(Request.class));
    }

    @Test
    public void sendMessage_shouldThrowIOException_callNotSuccessful() throws IOException {
        String apiVersion = "v1.0";
        String clientId = "client-id";
        MessageParams messageParams = MessageParams.builder().build();

        // mocking okhttp:
        Call call = mock(Call.class);
        Request request = mock(Request.class);
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("FAILED")
                .build();

        when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        assertThrows(IOException.class, () -> messengerAPIClient.sendMessage(apiVersion, clientId, messageParams));
        verify(okHttpClient).newCall(any(Request.class));
    }
}