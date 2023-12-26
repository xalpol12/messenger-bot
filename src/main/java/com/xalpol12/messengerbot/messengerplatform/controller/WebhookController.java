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
    public ResponseEntity<?> receiveWebhook(@RequestBody String body) {
        log.trace("Received webhook");
        webhookService.process(body);
        return ResponseEntity.ok().build();
    }

    @GetMapping(WebhookPath.ROOT)
    public ResponseEntity<?> verifyWebhook(@RequestParam("hub.mode") String mode,
                                           @RequestParam("token") String token,
                                           @RequestParam("hub.challenge")String challenge) {
        String receivedChallenge = webhookService.verifyWebhook(mode, token, challenge);
        return new ResponseEntity<>(receivedChallenge, HttpStatus.OK);
    }
}
