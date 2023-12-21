package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledMessageMapper {

    private ImageRepository imageRepository;
    private ModelMapper mapper;

    public ScheduledMessageMapper (ImageRepository imageRepository, ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.mapper = mapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
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


        TypeMap<ScheduledMessage, ScheduledMessageDTO> entityToDTOMapper =
                this.mapper.createTypeMap(ScheduledMessage.class, ScheduledMessageDTO.class);
        entityToDTOMapper.addMapping(ScheduledMessage::getId, ScheduledMessageDTO::setScheduledMessageId);
        entityToDTOMapper.addMappings(mapper -> mapper.map(src -> src.getImage().getId(), ScheduledMessageDTO::setImageUrl)); // TODO: Fix, return full URL with custom URI or id
    }

    public ScheduledMessage mapToScheduledMessage(ScheduledMessageDetails details) {
        return mapper.map(details, ScheduledMessage.class);
    }

    public ScheduledMessageDTO mapToScheduledMessageDTO(ScheduledMessage scheduledMessage) {
        return mapper.map(scheduledMessage, ScheduledMessageDTO.class);
    }

    public void updateScheduledMessage(ScheduledMessage source, ScheduledMessage destination) {
        mapper.map(source, destination);
    }
}
