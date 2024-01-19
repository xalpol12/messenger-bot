package com.xalpol12.messengerbot.messengerplatform.controller.docs;

import com.xalpol12.messengerbot.messengerplatform.controller.WebhookController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Webhook API", description = "API for receiving webhooks from Messenger Platform API, " +
        "should be hosted with secure HTTPS protocol. API constructed in accordance to the official Meta docs " +
        "available here: https://developers.facebook.com/docs/messenger-platform/webhooks")
public interface IWebhookController {

    class WebhookPath {
        public static final String ROOT = "/webhook";
    }

    @Operation(
            summary = "Receive webhook",
            description = "Receives webhook from Messenger Platform API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed received webhook, event received"),
            @ApiResponse(responseCode = "404", description = "Unknown or unexpected webhook object structure, couldn't deserialize the object"),
    })
    @PostMapping(WebhookController.WebhookPath.ROOT)
    ResponseEntity<String> receiveWebhook(@Parameter(name = "signature", description = "checksum for security verification")
                                     @RequestHeader("x-hub-signature-256") String signature,
                                     @Parameter(name = "body", description = "Page json that should be successfully deserialized to Webhook object")
                                     @RequestBody String body);

    @Operation(
            summary = "Receive new webhook subscription",
            description = "Receives webhook from Messenger Platform API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed notification about the new webhook subscription"),
            @ApiResponse(responseCode = "403", description = "Verification failed. Challenge or verification token didn't match the expected values"),
    })
    @GetMapping(WebhookController.WebhookPath.ROOT)
    ResponseEntity<String> verifyWebhookSubscription(@Parameter(name = "mode", description = "Should always have value \"subscription\"")
                                                @RequestParam("hub.mode") String mode,
                                                @Parameter(name = "token", description = "Verification token that should match the server-side verification token variable")
                                                @RequestParam("hub.verify_token") String token,
                                                @Parameter(name = "challenge", description = "Used as three-way handshake-like mechanism. Value is sent back to Messenger Platform API")
                                                @RequestParam("hub.challenge") String challenge);
}