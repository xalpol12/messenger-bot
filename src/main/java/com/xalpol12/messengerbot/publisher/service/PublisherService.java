package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import com.xalpol12.messengerbot.publisher.model.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

    private final ScheduledMessageRepository scheduledMessageRepository;
    private final SubscriberService subscriberService;
    private final FacebookPageAPIService facebookPageAPIService;

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
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Subscriber> subscribers = subscriberService.getAllSubscribers();

        for (Subscriber subscriber : subscribers) { //TODO: mark message as sent = true somehow
            // but it has to be sure that the message was sent, so do not mark it 'true' here i guess
            executorService.submit(() -> sendMessages(subscriber, messages));
        }
    }

    private void sendMessages(Subscriber subscriber, List<ScheduledMessage> scheduledMessages) {
        for (ScheduledMessage message : scheduledMessages) { //TODO: Another executor service???
            facebookPageAPIService.sendMessage(subscriber.getUserId(), message);
        }
    }
}
