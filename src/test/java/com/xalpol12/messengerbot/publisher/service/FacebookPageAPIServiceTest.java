package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class FacebookPageAPIServiceTest {

    @Autowired
    private FacebookPageAPIService facebookPageAPIService;

    @Test
    @Disabled
    public void shouldSendMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Test test");
        sb.append(": ");
        sb.append("https://raw.githubusercontent.com/xalpol12/where-to-eat-android-app/main/readme-scr/find_places.png");
        String message = sb.toString();
        String userId = "7760584580623854";
        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .id(1L)
                .scheduledDate(LocalDateTime.now())
                .message(message)
                .sent(false)
                .build();

        facebookPageAPIService.sendMessage(userId, scheduledMessage);
    }
}