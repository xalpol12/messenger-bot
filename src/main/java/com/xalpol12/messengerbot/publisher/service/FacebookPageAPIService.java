package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.publisher.model.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacebookPageAPIService {

    public List<Subscriber> getAllSubscribers() {
        return List.of(new Subscriber("user1"), new Subscriber("user2"), new Subscriber("user3"));
    }

    public void sendMessage(String userId, ScheduledMessage message) {
        log.info("User: {} received message: {}", userId, message.getMessage());
    }
}
