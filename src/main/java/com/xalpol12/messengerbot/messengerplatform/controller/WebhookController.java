package com.xalpol12.messengerbot.messengerplatform.controller;

import com.xalpol12.messengerbot.messengerplatform.controller.docs.IWebhookController;
import com.xalpol12.messengerbot.messengerplatform.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController implements IWebhookController {

    private final WebhookService webhookService;

    @Operation(
            summary = "Receive webhook",
            description = "Receives webhook from Messenger Platform API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed received webhook, event received"),
            @ApiResponse(responseCode = "404", description = "Unknown or unexpected webhook object structure, couldn't deserialize the object"),
    })
    @PostMapping(WebhookPath.ROOT)
    public ResponseEntity<String> receiveWebhook(String signature, String body) {
        log.trace("Received webhook");
//        webhookService.verifyRequestSignature(signature, body);
        webhookService.process(body);
        return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
    }

    public ResponseEntity<String> verifyWebhookSubscription(String mode, String token, String challenge) {
        String receivedChallenge = webhookService.verifyWebhookSubscription(mode, token, challenge);
        return new ResponseEntity<>(receivedChallenge, HttpStatus.OK);
    }
}
