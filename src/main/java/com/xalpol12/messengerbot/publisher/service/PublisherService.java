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
    private final FacebookPageAPIService fbService;

    @Scheduled(fixedRate = 60000)
    public void selectScheduledMessages() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime minuteBefore = currentTime.minusMinutes(1);
        LocalDateTime minuteAfter = currentTime.plusMinutes(1);

        log.info("Invoked scheduled service method at: {}", currentTime);

        List<ScheduledMessage> messagesToSend =
                scheduledMessageRepository.findAllByScheduledDateBetweenAndSentIsFalse(minuteBefore, minuteAfter);

        log.debug("Found: {} scheduled messages!", messagesToSend.size());

        if (messagesToSend.size() > 0) {
            submitMessages(messagesToSend);
            log.debug("Sending...");
        }
    }

    private void submitMessages(List<ScheduledMessage> messages) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Subscriber> subscribers = fbService.getAllSubscribers();

        for (Subscriber subscriber : subscribers) { //TODO: mark message as sent = true somehow
            // but it has to be sure that the message was sent, so do not mark it 'true' here i guess
            executorService.submit(() -> sendMessages(subscriber, messages));
        }
    }

    private void sendMessages(Subscriber subscriber, List<ScheduledMessage> scheduledMessages) {
        for (ScheduledMessage message : scheduledMessages) { //TODO: Another executor service???
            fbService.sendMessage(subscriber.getUserId(), message);
        }
    }
}
