package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ScheduledMessageMapperTest {

    private static ScheduledMessageMapper scheduledMessageMapper;

    @BeforeAll
    public static void setup() {
        ImageRepository imageRepository = mock(ImageRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        scheduledMessageMapper = new ScheduledMessageMapper(imageRepository, modelMapper);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void mapToScheduledMessage_shouldReturnScheduledMessage() {
        ScheduledMessageDetails details = new ScheduledMessageDetails();

        ScheduledMessage result = scheduledMessageMapper.mapToScheduledMessage(details);

        assertNotNull(result);
    }

    @Test
    public void mapToScheduledMessageDTO_shouldReturnScheduledMessageDTO_customImageUriProvided() {
        Image image = new Image();
        image.setCustomUri("custom-uri");
        String expectedImageUrl = "http://localhost/api/image/custom-uri";
        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .id(1L)
                .scheduledDate(LocalDateTime.MIN)
                .message("test-msg")
                .image(image)
                .sent(false)
                .build();

        ScheduledMessageDTO result = scheduledMessageMapper.mapToScheduledMessageDTO(scheduledMessage);

        assertAll(() -> {
            assertEquals(scheduledMessage.getId(), result.getScheduledMessageId());
            assertEquals(scheduledMessage.getScheduledDate(), result.getScheduledDate());
            assertEquals(scheduledMessage.getMessage(), result.getMessage());
            assertEquals(expectedImageUrl, result.getImageUrl());
            assertEquals(scheduledMessage.isSent(), result.isSent());
        });
    }

    @Test
    public void mapToScheduledMessageDTO_shouldReturnScheduledMessageDTO_noCustomImageUriProvided() {
        Image image = new Image();
        image.setId("id");
        String expectedImageUrl = "http://localhost/api/image/id";
        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .id(1L)
                .scheduledDate(LocalDateTime.MIN)
                .message("test-msg")
                .image(image)
                .sent(false)
                .build();

        ScheduledMessageDTO result = scheduledMessageMapper.mapToScheduledMessageDTO(scheduledMessage);

        assertAll(() -> {
            assertEquals(scheduledMessage.getId(), result.getScheduledMessageId());
            assertEquals(scheduledMessage.getScheduledDate(), result.getScheduledDate());
            assertEquals(scheduledMessage.getMessage(), result.getMessage());
            assertEquals(expectedImageUrl, result.getImageUrl());
            assertEquals(scheduledMessage.isSent(), result.isSent());
        });
    }

    @Test
    public void mapToScheduledMessageDTO_shouldReturnScheduledMessageDTO_noImageProvided() {
        ScheduledMessage scheduledMessage = ScheduledMessage.builder()
                .id(1L)
                .scheduledDate(LocalDateTime.MIN)
                .message("test-msg")
                .image(null)
                .sent(false)
                .build();

        ScheduledMessageDTO result = scheduledMessageMapper.mapToScheduledMessageDTO(scheduledMessage);

        assertAll(() -> {
            assertEquals(scheduledMessage.getId(), result.getScheduledMessageId());
            assertEquals(scheduledMessage.getScheduledDate(), result.getScheduledDate());
            assertEquals(scheduledMessage.getMessage(), result.getMessage());
            assertNull(result.getImageUrl());
            assertEquals(scheduledMessage.isSent(), result.isSent());
        });
    }
}