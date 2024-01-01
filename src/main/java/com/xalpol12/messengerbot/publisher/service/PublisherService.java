package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import com.xalpol12.messengerbot.publisher.model.Subscriber;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

    @Value("${base.server.address}")
    private String BASE_SERVER_ADDRESS; // yes, i know it's cringe, but it's the easiest way...

    private final ExecutorService messagesExecutor;
    private final ExecutorService subscribersExecutor;
    private final ScheduledMessageRepository scheduledMessageRepository;
    private final SubscriberService subscriberService;
    private final FacebookPageAPIService facebookPageAPIService;
    private final ImageMapper imageMapper;

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

        for (ScheduledMessage scheduledMessage : messages) {
            String message = extractMessageWithImageLink(scheduledMessage);
            messagesExecutor.submit(() -> sendMessageToAllSubscribers(subscribers, message));
            scheduledMessage.setSent(true); //TODO: Wait for messagesExecutor full execution
        }
    }

    private void sendMessageToAllSubscribers(List<Subscriber> subscribers, String message) {
        for (Subscriber subscriber : subscribers) {
            subscribersExecutor.submit(() -> sendMessage(subscriber, message));
        }
    }

    private void sendMessage(Subscriber subscriber, String message) {
        String userId = subscriber.getUserId();
        facebookPageAPIService.sendMessage(userId, message);
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
