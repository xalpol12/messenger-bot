package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.exception.customexception.ScheduledMessageNotFoundException;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ScheduledMessageMapper;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import jakarta.transaction.Transactional;
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

    /**
     * Retrieves a scheduled message from the database.
     * @param messageId Long identifier of the scheduled message
     * @return ScheduledMessageDTO representation of the scheduled message
     */
    public ScheduledMessageDTO getScheduledMessage(Long messageId) {
        ScheduledMessage message = findByIdOrThrowException(messageId);
        return messageMapper.mapToScheduledMessageDTO(message);
    }

    private ScheduledMessage findByIdOrThrowException(Long messageId) {
        return scheduledMessageRepository.findById(messageId)
                .orElseThrow(() -> new ScheduledMessageNotFoundException("Scheduled message with id: " + messageId + " does not exist"));
    }

    /**
     * Retrieves all scheduled messages from the database.
     * @return List<ScheduledMessageDTO> the DTO representations of scheduled messages
     */
    public List<ScheduledMessageDTO> getAllScheduledMessages() {
        Stream<ScheduledMessage> messageStream = scheduledMessageRepository.findAll().stream();
        return messageStream
                .map(messageMapper::mapToScheduledMessageDTO)
                .collect(Collectors.toList());
    }

    /**
     * Adds new ScheduledMessage entity to a database.
     * @param messageDetails ScheduledMessageDetails instance
     * @return ScheduledMessageDTO the DTO representation of added entity
     */
    public ScheduledMessageDTO addScheduledMessage(ScheduledMessageDetails messageDetails) {
        ScheduledMessage message = messageMapper.mapToScheduledMessage(messageDetails);
        scheduledMessageRepository.save(message);
        log.info("Added new scheduled image with identifier: {}", message.getId());
        return messageMapper.mapToScheduledMessageDTO(message);
    }

    /**
     * Deletes ScheduledMessage entity from the database.
     * @param messageId Long identifier of the scheduled message
     * @throws ScheduledMessageNotFoundException no scheduled message with
     * given identifier found in the database
     */
    public void deleteScheduledMessage(Long messageId) throws ScheduledMessageNotFoundException {
        if (scheduledMessageRepository.existsById(messageId)) {
            scheduledMessageRepository.deleteById(messageId);
        } else {
            throw new ScheduledMessageNotFoundException("No scheduled message found for entity with id: " + messageId);
        }
        log.info("Deleted scheduled image with identifier: {}", messageId);
    }

    /**
     * Batch delete all entities from repository
     * based on the provided list.
     * @param messageIds List of entity ids marked for deletion
     */
    @Transactional
    public void deleteSelectedMessages(List<Long> messageIds) {
        scheduledMessageRepository.deleteAllInScheduledMessageIdList(messageIds);
        log.info("All messages specified by " +
                "message Ids list have been deleted");
    }

    /**
     * Deletes all ScheduledMessage entities from the database.
     */
    public void deleteAllScheduledMessages() {
        scheduledMessageRepository.deleteAll();
        log.info("Deleted all scheduled images");
    }

    /**
     * Updates ScheduledMessage entity by identifier.
     * @param messageId Long identifier of the scheduled message
     * @param details ScheduledMessageDetails instance that the entity
     *                should be updated with
     */
    public void updateScheduledMessage(Long messageId,
                                       ScheduledMessageDetails details) {
        ScheduledMessage originalMessage = findByIdOrThrowException(messageId);
        ScheduledMessage updatedMessage = messageMapper.mapToScheduledMessage(details);
        messageMapper.updateScheduledMessage(updatedMessage, originalMessage);
        log.info("Updated scheduled image with identifier: {}", messageId);
    }
}
