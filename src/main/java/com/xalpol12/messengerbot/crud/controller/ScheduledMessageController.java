package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.controller.docs.IScheduledMessageController;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledMessageController implements IScheduledMessageController {

    private final ScheduledMessageService scheduledMessageService;

    public ResponseEntity<ScheduledMessageDTO> getScheduledMessage(Long messageId) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.getScheduledMessage(messageId);
        log.trace("GET /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok(messageDTO);
    }

    public ResponseEntity<List<ScheduledMessageDTO>> getAllScheduledMessages() {
        List<ScheduledMessageDTO> messages = scheduledMessageService.getAllScheduledMessages();
        log.trace("GET /scheduled-messages returned {} elements", messages.size());
        return ResponseEntity.ok(messages);
    }

    public ResponseEntity<ScheduledMessageDTO> uploadScheduledMessage(ScheduledMessageDetails scheduledMessageDetails) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.addScheduledMessage(scheduledMessageDetails);
        log.trace("POST /scheduled-message called");
        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteScheduledMessage(Long messageId) {
        scheduledMessageService.deleteScheduledMessage(messageId);
        log.trace("DELETE /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> deleteAllScheduledMessages() {
        scheduledMessageService.deleteAllScheduledMessages();
        log.trace("DELETE /scheduled-messages called");
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> updateScheduledMessage(Long messageId, ScheduledMessageDetails messageDetails) {
        scheduledMessageService.updateScheduledMessage(messageId, messageDetails);
        log.trace("PUT /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok().build();
    }
}