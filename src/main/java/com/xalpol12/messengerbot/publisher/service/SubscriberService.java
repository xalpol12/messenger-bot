package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service class used for managing all subscribers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    /**
     * Returns all subscribers from the database
     * @return List<Subscriber>
     */
    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    /**
     * Adds new subscribers to the database.
     * @param senderIds Set of sender IDs, only
     *                  senders that were not previously
     *                  in the database are added
     */
    public void addNewSubscribers(Set<String> senderIds) {
        for (String id : senderIds) {
            if (!subscriberRepository.existsById(id)) {
                subscriberRepository.save(new Subscriber(id));
            }
        }
    }
}
