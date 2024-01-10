package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import com.xalpol12.messengerbot.publisher.exception.MessagePublishingException;
import com.xalpol12.messengerbot.publisher.model.Subscriber;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Service class responsible for publishing scheduled messages to all subscribers.
 * Uses scheduled tasks to select messages at regular intervals and submits them for publication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

    @Value("${base.server.address}")
    private String BASE_SERVER_ADDRESS; // yes, i know it's cringe, but it's the easiest way, without controller context

    private final ExecutorService messagesExecutor;
    private final ExecutorService subscribersExecutor;
    private final ScheduledMessageRepository scheduledMessageRepository;
    private final SubscriberService subscriberService;
    private final FacebookPageAPIService facebookPageAPIService;
    private final ImageMapper imageMapper;

    /**
     * Selects scheduled messages within a minute time window and publishes them to all subscribers.
     * Scheduled to run at a fixed rate specified by the 'fixedRate' attribute (60,000 milliseconds by default).
     */
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
        }
    }

    /**
     * Submits a list of scheduled messages for publication to all subscribers.
     * @param scheduledMessages List of scheduled messages to be published.
     */
    @Transactional
    public void submitMessages(List<ScheduledMessage> scheduledMessages) {
        List<Subscriber> subscribers = subscriberService.getAllSubscribers();
        CountDownLatch messagesLatch = new CountDownLatch(scheduledMessages.size());

        for (ScheduledMessage scheduledMessage : scheduledMessages) {
            String message = extractMessageWithImageLink(scheduledMessage);
            messagesExecutor.submit(() -> {
                        sendMessageToAllSubscribers(subscribers, message);
                        messagesLatch.countDown();
                    }
            );
        }

        try {
            messagesLatch.await();
            for (ScheduledMessage m : scheduledMessages) {
                m.setSent(true);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error waiting for messages to be sent", e);
        }

        log.info("All scheduled messages published successfully");
    }

    private void sendMessageToAllSubscribers(List<Subscriber> subscribers, String message) {
        CountDownLatch subscribersLatch = new CountDownLatch(subscribers.size());

        for (Subscriber subscriber : subscribers) {
            subscribersExecutor.submit(() -> {
                sendMessage(subscriber, message);
                subscribersLatch.countDown();
            });
        }

        try {
            subscribersLatch.await();
            log.trace("Latch counted down, message has been sent to all subscribers. Message body: {}", message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error waiting for subscribers to receive the message", e);
        }
    }

    private void sendMessage(Subscriber subscriber, String message) {
        String userId = subscriber.getUserId();
        try {
            facebookPageAPIService.sendMessage(userId, message);
        } catch (MessagePublishingException e) {
            log.error(e.getMessage());
        }
    }

    private String extractMessageWithImageLink(ScheduledMessage scheduledMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(scheduledMessage.getMessage());
        Image image = scheduledMessage.getImage();
        if (image != null) {
            ImageDTO imageDTO = imageMapper.mapToImageDTO(image, BASE_SERVER_ADDRESS);
            sb.append(" ");
            sb.append(imageDTO.getUrl());
        }
        return sb.toString();
    }
}
