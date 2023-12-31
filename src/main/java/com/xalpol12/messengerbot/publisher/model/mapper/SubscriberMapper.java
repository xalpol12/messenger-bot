package com.xalpol12.messengerbot.publisher.model.mapper;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.model.dto.SubscriberDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriberMapper {

    private ModelMapper mapper;

    public Subscriber mapToSubscriber(SubscriberDetails details) {
        return mapper.map(details, Subscriber.class);
    }
}
