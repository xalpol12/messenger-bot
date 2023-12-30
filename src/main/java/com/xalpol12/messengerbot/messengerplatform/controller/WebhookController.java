package com.xalpol12.messengerbot.messengerplatform.controller;

import com.xalpol12.messengerbot.messengerplatform.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    public static class WebhookPath {
        public static final String ROOT = "/webhook";
        private WebhookPath() {}
    }

    @PostMapping(WebhookPath.ROOT)
    public ResponseEntity<?> receiveWebhook(
            @RequestHeader("x-hub-signature-256") String signature,
            @RequestBody String body) {
        log.trace("Received webhook");
//        webhookService.verifyRequestSignature(signature, body);
        webhookService.process(body);
        return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
    }

    @GetMapping(WebhookPath.ROOT)
    public ResponseEntity<?> verifyWebhookSubscription(@RequestParam("hub.mode") String mode,
                                                       @RequestParam("hub.verify_token") String token,
                                                       @RequestParam("hub.challenge")String challenge) {
        String receivedChallenge = webhookService.verifyWebhookSubscription(mode, token, challenge);
        return new ResponseEntity<>(receivedChallenge, HttpStatus.OK);
    }
}
