package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ScheduledMessageMapper;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledMessageService {

    private final ScheduledMessageRepository scheduledMessageRepository;
    private final ScheduledMessageMapper messageMapper;

    public ScheduledMessageDTO getScheduledMessage(Long messageId) {
        ScheduledMessage message = findByIdOrThrowException(messageId);
        return messageMapper.mapToScheduledMessageDTO(message);
    }

    private ScheduledMessage findByIdOrThrowException(Long messageId) {
        return scheduledMessageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Scheduled message with id: " + messageId + " does not exist"));
    }

    public List<ScheduledMessageDTO> getAllScheduledMessages() {
        Stream<ScheduledMessage> messageStream = scheduledMessageRepository.findAll().stream();
        return messageStream
                .map(messageMapper::mapToScheduledMessageDTO)
                .collect(Collectors.toList());
    }

    public ScheduledMessageDTO addScheduledMessage(ScheduledMessageDetails messageDetails) {
        ScheduledMessage message = messageMapper.mapToScheduledMessage(messageDetails);
        scheduledMessageRepository.save(message);
        log.info("Added new scheduled image with identifier: {}", message.getId());
        return messageMapper.mapToScheduledMessageDTO(message);
    }

    public void deleteScheduledMessage(Long messageId) throws EntityNotFoundException {
        if (scheduledMessageRepository.existsById(messageId)) {
            scheduledMessageRepository.deleteById(messageId);
        } else {
            throw new EntityNotFoundException("No scheduled message found for entity with id: " + messageId);
        }
        log.info("Deleted scheduled image with identifier: {}", messageId);
    }

    public void updateScheduledMessage(Long messageId,
                                       ScheduledMessageDetails details) {
        ScheduledMessage originalMessage = findByIdOrThrowException(messageId);
        ScheduledMessage updatedMessage = messageMapper.mapToScheduledMessage(details);
        messageMapper.updateScheduledMessage(updatedMessage, originalMessage);
        log.info("Updated scheduled image with identifier: {}", messageId);
    }
}
