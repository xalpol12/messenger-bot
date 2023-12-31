package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.model.dto.SubscriberDetails;
import com.xalpol12.messengerbot.publisher.model.mapper.SubscriberMapper;
import com.xalpol12.messengerbot.publisher.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SubscriberMapper mapper;

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    public void addNewSubscribers(Set<String> senderIds) {
        for (String id : senderIds) {
            if (!subscriberRepository.existsById(id)) {
                subscriberRepository.save(new Subscriber(id));
            }
        }
    }
}
