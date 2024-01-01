package com.xalpol12.messengerbot.publisher.client;

import com.xalpol12.messengerbot.messengerplatform.model.detail.Message;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import com.xalpol12.messengerbot.messengerplatform.model.dto.MessageResponse;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "messenger-api-client",
        url = "https://graph.facebook.com"
)
public interface MessengerAPIClient {

    @PostMapping(value = "/{version}/{clientId}/messages")
    MessageResponse sendMessage(@PathVariable("version") String apiVersion,
                                @PathVariable("clientId") String clientId,
                                @RequestParam("recipient") String recipient,
                                @RequestParam("message") String message,
                                @RequestParam("messaging_type") String messaging_type,
                                @RequestParam("access_token") String access_token);
}
