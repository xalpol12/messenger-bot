package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledMessageController {

    static class ScheduledMessagePath {
        public static final String ROOT = "/api/scheduled-message";
        private ScheduledMessagePath() {};
    }

    private final ScheduledMessageService scheduledMessageService;

    @GetMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<ScheduledMessageDTO> getScheduledMessage(@PathVariable("id") Long messageId) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.getScheduledMessage(messageId);
        log.trace("GET /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok(messageDTO);
    }

    @GetMapping(ScheduledMessagePath.ROOT + "s")
    public ResponseEntity<List<ScheduledMessageDTO>> getAllScheduledMessages() {
        List<ScheduledMessageDTO> messages = scheduledMessageService.getAllScheduledMessages();
        log.trace("GET /scheduled-messages returned {} elements", messages.size());
        return ResponseEntity.ok(messages);
    }

    @PostMapping(ScheduledMessagePath.ROOT)
    public ResponseEntity<ScheduledMessageDTO> uploadScheduledMessage(@RequestBody ScheduledMessageDetails scheduledMessageDetails) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.addScheduledMessage(scheduledMessageDetails);
        log.trace("POST /scheduled-message called");
        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<?> deleteScheduledMessage(@PathVariable("id") Long messageId) {
        scheduledMessageService.deleteScheduledMessage(messageId);
        log.trace("DELETE /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ScheduledMessagePath.ROOT + "s")
    public ResponseEntity<?> deleteAllScheduledMessages() {
        scheduledMessageService.deleteAllScheduledMessages();
        log.trace("DELETE /scheduled-messages called");
        return ResponseEntity.noContent().build();
    }

    @PutMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<?> updateScheduledMessage(@PathVariable("id") Long messageId,
                                                    @RequestBody ScheduledMessageDetails messageDetails) {
        scheduledMessageService.updateScheduledMessage(messageId, messageDetails);
        log.trace("PUT /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok().build();
    }
}
