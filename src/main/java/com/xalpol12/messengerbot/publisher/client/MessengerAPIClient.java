package com.xalpol12.messengerbot.publisher.client;

import com.xalpol12.messengerbot.messengerplatform.model.detail.Message;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "messenger-api-client",
        url = "https://graph.facebook.com"
)
public interface MessengerAPIClient {

    @PostMapping(value = "/{version}/{clientId}/messages")
    MessageResponse sendMessage(@PathVariable("version") String apiVersion,
                                @PathVariable("clientId") String clientId,
                                @RequestParam Subject recipient,
                                @RequestParam Message message,
                                @RequestParam String messaging_type,
                                @RequestParam String access_token);
}
