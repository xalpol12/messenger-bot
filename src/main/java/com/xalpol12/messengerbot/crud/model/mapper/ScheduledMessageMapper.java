package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledMessageMapper {

    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    public ScheduledMessage mapToScheduledMessage(ScheduledMessageDetails details) {
        Image image = imageRepository.getReferenceById(details.getImageId());
        return ScheduledMessage.builder()
                .scheduledDate(LocalDateTime.parse(details.getScheduledDate()))
                .message(details.getScheduledDate())
                .image(image)
                .isSent(false)
                .build();
    }

    public ScheduledMessageDTO mapToScheduledMessageDTO(ScheduledMessage scheduledMessage) {
        return mapper.map(scheduledMessage, ScheduledMessageDTO.class);
    }

    public void updateScheduledMessage(ScheduledMessage source, ScheduledMessage destination) {
        mapper.map(source, destination);
    }
}
