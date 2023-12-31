package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }
}
