package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.exception.ScheduledMessageNotFoundException;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ScheduledMessageMapper;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduledMessageServiceTest {

    @Mock
    private ScheduledMessageRepository repository;
    @Mock
    private ScheduledMessageMapper mapper;

    private ScheduledMessageService scheduledMessageService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        scheduledMessageService = new ScheduledMessageService(repository, mapper);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void getScheduledMessage_shouldReturnScheduledMessageDTO() {
        Long id = 1L;
        ScheduledMessage message = ScheduledMessage.builder().id(id).scheduledDate(LocalDateTime.MAX).build();
        ScheduledMessageDTO dto = ScheduledMessageDTO.builder().scheduledMessageId(id).build();

        when(repository.findById(id)).thenReturn(Optional.of(message));
        when(mapper.mapToScheduledMessageDTO(message)).thenReturn(dto);

        ScheduledMessageDTO resultDto = scheduledMessageService.getScheduledMessage(id);

        assertAll(() -> {
            verify(repository, times(1)).findById(id);
            verify(mapper, times(1)).mapToScheduledMessageDTO(message);
            assertEquals(dto, resultDto);
        });
    }
    @Test
    public void getScheduledMessage_shouldThrowEntityNotFoundException() {
        Long id = 1L;
        ScheduledMessage message = ScheduledMessage.builder().id(id).scheduledDate(LocalDateTime.MAX).build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertAll(() -> {
            assertThrows(ScheduledMessageNotFoundException.class, () -> scheduledMessageService.getScheduledMessage(id));
            verify(repository, times(1)).findById(id);
        });
    }

    @Test
    public void getAllScheduledMessages_shouldReturnListOfMessages() {
        when(repository.findAll()).thenReturn(List.of(new ScheduledMessage(), new ScheduledMessage()));
        when(mapper.mapToScheduledMessageDTO(any())).thenReturn(new ScheduledMessageDTO());

        List<ScheduledMessageDTO> results = scheduledMessageService.getAllScheduledMessages();

        assertAll(() -> {
            verify(mapper, times(2)).mapToScheduledMessageDTO(any());
            assertEquals(2, results.size());
        });
    }

    @Test
    public void addScheduledMessage_shouldCallRepositorySave() {
        ScheduledMessageDetails scheduledMessageDetails = new ScheduledMessageDetails();
        ScheduledMessage scheduledMessage = new ScheduledMessage();

        when(mapper.mapToScheduledMessage(scheduledMessageDetails)).thenReturn(scheduledMessage);

        scheduledMessageService.addScheduledMessage(scheduledMessageDetails);

        verify(repository, times(1)).save(scheduledMessage);
    }

    @Test
    public void deleteScheduledMessage_shouldCallDeleteByIdMethod() {
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);
        scheduledMessageService.deleteScheduledMessage(id);

        assertAll(() -> {
            verify(repository, times(1)).existsById(id);
            verify(repository, times(1)).deleteById(id);
        });
    }

    @Test
    public void deleteAllScheduledMessages_shouldCallMethod() {

        scheduledMessageService.deleteAllScheduledMessages();

        assertAll(() -> {
            verify(repository, times(1)).deleteAll();
        });
    }
    @Test
    public void deleteScheduledMessage_shouldThrowEntityNotFoundException() {
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(false);

        assertAll(() -> {
            assertThrows(ScheduledMessageNotFoundException.class, () -> scheduledMessageService.deleteScheduledMessage(id));
            verify(repository, times(1)).existsById(id);
        });
    }

    @Test
    public void updateScheduledMessage_shouldCallUpdateScheduledMessageMapperMethod() {
        Long id = 1L;
        ScheduledMessageDetails details = new ScheduledMessageDetails();
        ScheduledMessage message = new ScheduledMessage();

        when(repository.findById(id)).thenReturn(Optional.of(message));
        when(mapper.mapToScheduledMessage(details)).thenReturn(message);

        scheduledMessageService.updateScheduledMessage(id, details);

        assertAll(() -> {
            verify(repository, times(1)).findById(id);
            verify(mapper, times(1)).mapToScheduledMessage(details);
            verify(mapper, times(1)).updateScheduledMessage(message, message);
        });
    }
}