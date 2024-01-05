package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.controller.ImageController;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

/**
 * ModelMapper wrapper class that encapsulates
 * logic for mapping ScheduledMessage entity class to different
 * forms.
 */
@Component
public class ScheduledMessageMapper {

    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    public ScheduledMessageMapper (ImageRepository imageRepository, ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.mapper = mapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        // ScheduledMessageDetails -> ScheduledMessage
        TypeMap<ScheduledMessageDetails, ScheduledMessage> inputToEntityMapper =
                this.mapper.createTypeMap(ScheduledMessageDetails.class, ScheduledMessage.class);

        inputToEntityMapper.addMappings(mapper -> {
            Converter<String, LocalDateTime> dateConverter = ctx -> {
                String scheduledDate = ctx.getSource();
                return scheduledDate != null ? LocalDateTime.parse(scheduledDate) : LocalDateTime.MIN;
            };
            mapper.using(dateConverter).map(ScheduledMessageDetails::getScheduledDate, ScheduledMessage::setScheduledDate);
        });

        inputToEntityMapper.addMappings(mapper -> {
            Converter<String, Image> imageConverter = ctx -> {
                String imageId = ctx.getSource();
                return imageId != null ? imageRepository.getReferenceById(imageId) : null;
            };
            mapper.using(imageConverter).map(ScheduledMessageDetails::getImageId, ScheduledMessage::setImage);
        });
    }

    /**
     * Returns new ScheduledMessage using ScheduledMessageDetails.
     * @param details ScheduledMessageDetails instance
     * @return ScheduledMessage instance
     */
    public ScheduledMessage mapToScheduledMessage(ScheduledMessageDetails details) {
        return mapper.map(details, ScheduledMessage.class);
    }

    /**
     * Returns new ScheduledMessageDTO based on provided ScheduledMessage entity,
     * mapping only fields present in DTO. If no Image is associated with provided
     * ScheduledMessage, then imageUrl field is set to null.
     * @param scheduledMessage ScheduledMessage entity
     * @return ScheduledMessageDTO instance based on scheduledMessage
     */
    public ScheduledMessageDTO mapToScheduledMessageDTO(ScheduledMessage scheduledMessage) {
         return ScheduledMessageDTO.builder()
                 .scheduledMessageId(scheduledMessage.getId())
                 .scheduledDate(scheduledMessage.getScheduledDate())
                 .message(scheduledMessage.getMessage())
                 .imageUrl(getImageUrl(scheduledMessage))
                 .isSent(scheduledMessage.isSent())
                 .build();
    }

    private String getImageUrl(ScheduledMessage scheduledMessage) {
        if (scheduledMessage.getImage() != null) {
            Image image = scheduledMessage.getImage();
            String uri = image.getCustomUri() != null ? image.getCustomUri() : image.getId();
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(ImageController.ImagePath.ROOT)
                    .path("/" + uri)
                    .toUriString();
        } else return null;
    }

    /**
     * Maps ScheduledMessage with another ScheduledMessage.
     * @param source ScheduledMessage entity
     * @param destination ScheduledMessage entity
     */
    public void updateScheduledMessage(ScheduledMessage source, ScheduledMessage destination) {
        mapper.map(source, destination);
    }
}
