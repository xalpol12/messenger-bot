package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import com.xalpol12.messengerbot.publisher.model.Subscriber;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

    private final ExecutorService messagesExecutor;
    private final ExecutorService subscribersExecutor;
    private final ScheduledMessageRepository scheduledMessageRepository;
    private final SubscriberService subscriberService;
    private final FacebookPageAPIService facebookPageAPIService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void selectScheduledMessages() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime minuteBefore = currentTime.minusMinutes(1);
        LocalDateTime minuteAfter = currentTime.plusMinutes(1);

        List<ScheduledMessage> messagesToSend =
                scheduledMessageRepository.findAllByScheduledDateBetweenAndSentIsFalse(minuteBefore, minuteAfter);

        if (messagesToSend.size() > 0) {
            log.debug("Found: {} scheduled messages!", messagesToSend.size());
            submitMessages(messagesToSend);
            log.debug("Sending...");
        }
    }

    private void submitMessages(List<ScheduledMessage> messages) {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();

        for (ScheduledMessage message : messages) {
            messagesExecutor.submit(() -> sendMessageToAllSubscribers(message, subscribers));
            message.setSent(true);
        }
    }

    private void sendMessageToAllSubscribers(ScheduledMessage message, List<Subscriber> subscribers) {
        for (Subscriber subscriber : subscribers) {
            subscribersExecutor.submit(() -> sendMessage(message, subscriber));
        }
    }

    private void sendMessage(ScheduledMessage message, Subscriber subscriber) {
        String userId = subscriber.getUserId();
        facebookPageAPIService.sendMessage(userId, message);
    }
}
