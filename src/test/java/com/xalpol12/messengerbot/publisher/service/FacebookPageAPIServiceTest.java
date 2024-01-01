package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class FacebookPageAPIServiceTest {

    @Autowired
    private FacebookPageAPIService facebookPageAPIService;

    @Test
    public void shouldSendMessage() {
        String userId = "7760584580623854";
        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .id(1L)
                .scheduledDate(LocalDateTime.now())
                .message("Test test")
                .sent(false)
                .build();

        facebookPageAPIService.sendMessage(userId, scheduledMessage);
    }
}