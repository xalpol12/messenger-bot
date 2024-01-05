package com.xalpol12.messengerbot.publisher.model.mapper;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.model.dto.SubscriberDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * ModelMapper wrapper class that maps
 * classes related to Subscriber entity.
 */
@Component
@RequiredArgsConstructor
public class SubscriberMapper {

    private ModelMapper mapper;

    /**
     * Maps SubscriberDetails to Subscriber instance
     * @param details SubscriberDetails instance
     * @return new Subscriber instance
     */
    public Subscriber mapToSubscriber(SubscriberDetails details) {
        return mapper.map(details, Subscriber.class);
    }
}
