package com.xalpol12.messengerbot.messengerplatform.controller;

import com.xalpol12.messengerbot.messengerplatform.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Webhook API", description = "API for receiving webhooks from Messenger Platform API, " +
        "should be hosted with secure HTTPS protocol. API constructed in accordance to the official Meta docs " +
        "available here: https://developers.facebook.com/docs/messenger-platform/webhooks")
@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    public static class WebhookPath {
        public static final String ROOT = "/webhook";
        private WebhookPath() {}
    }

    @Operation(
            summary = "Receive webhook",
            description = "Receives webhook from Messenger Platform API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed received webhook, event received"),
            @ApiResponse(responseCode = "404", description = "Unknown or unexpected webhook object structure, couldn't deserialize the object"),
    })
    @PostMapping(WebhookPath.ROOT)
    public ResponseEntity<?> receiveWebhook(
            @Parameter(name = "signature", description = "checksum for security verification")
            @RequestHeader("x-hub-signature-256") String signature,
            @Parameter(name = "body", description = "Page json that should be successfully deserialized to Webhook object")
            @RequestBody String body) {
        log.trace("Received webhook");
//        webhookService.verifyRequestSignature(signature, body);
        webhookService.process(body);
        return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
    }
    @Operation(
            summary = "Receive new webhook subscription",
            description = "Receives webhook from Messenger Platform API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed notification about the new webhook subscription"),
            @ApiResponse(responseCode = "403", description = "Verification failed. Challenge or verification token didn't match the expected values"),
    })
    @GetMapping(WebhookPath.ROOT)
    public ResponseEntity<?> verifyWebhookSubscription(
            @Parameter(name = "mode", description = "Should always have value \"subscription\"")
            @RequestParam("hub.mode") String mode,
            @Parameter(name = "token", description = "Verification token that should match the server-side verification token variable")
            @RequestParam("hub.verify_token") String token,
            @Parameter(name = "challenge", description = "Used as three-way handshake-like mechanism. Value is sent back to Messenger Platform API")
            @RequestParam("hub.challenge")String challenge) {
        String receivedChallenge = webhookService.verifyWebhookSubscription(mode, token, challenge);
        return new ResponseEntity<>(receivedChallenge, HttpStatus.OK);
    }
}
