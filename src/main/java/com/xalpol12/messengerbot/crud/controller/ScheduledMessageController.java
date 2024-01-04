package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Scheduled Messages API", description = "API for scheduling messages to be sent through Messenger")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledMessageController {

    static class ScheduledMessagePath {
        public static final String ROOT = "/api/scheduled-message";
        private ScheduledMessagePath() {};
    }

    private final ScheduledMessageService scheduledMessageService;

    @Operation(
            summary = "Return scheduled message",
            description = "Returns scheduled message details in form of ScheduledMessageDTO based on provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @GetMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<ScheduledMessageDTO> getScheduledMessage(
            @Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
            @PathVariable("id") Long messageId) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.getScheduledMessage(messageId);
        log.trace("GET /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok(messageDTO);
    }

    @Operation(
            summary = "Return all scheduled messages",
            description = "Returns all scheduled message details in form of ScheduledMessageDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all entities from the database"),
    })
    @GetMapping(ScheduledMessagePath.ROOT + "s")
    public ResponseEntity<List<ScheduledMessageDTO>> getAllScheduledMessages() {
        List<ScheduledMessageDTO> messages = scheduledMessageService.getAllScheduledMessages();
        log.trace("GET /scheduled-messages returned {} elements", messages.size());
        return ResponseEntity.ok(messages);
    }

    @Operation(
            summary = "Upload scheduled message",
            description = "Saves new ScheduledMessage entity in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved new entity in the database"),
    }) // TODO: check other exceptions in this endpoint
    @PostMapping(ScheduledMessagePath.ROOT)
    public ResponseEntity<ScheduledMessageDTO> uploadScheduledMessage(
            @Parameter(name = "scheduled message details", description = "ScheduledMessageDetails object")
            @RequestBody ScheduledMessageDetails scheduledMessageDetails) {
        ScheduledMessageDTO messageDTO = scheduledMessageService.addScheduledMessage(scheduledMessageDetails);
        log.trace("POST /scheduled-message called");
        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete scheduled message",
            description = "Deletes scheduled message with given ID from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @DeleteMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<?> deleteScheduledMessage(
            @Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
            @PathVariable("id") Long messageId) {
        scheduledMessageService.deleteScheduledMessage(messageId);
        log.trace("DELETE /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete all scheduled message",
            description = "Deletes all scheduled messages from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the ScheduledMessage entity from the database"),
    })
    @DeleteMapping(ScheduledMessagePath.ROOT + "s")
    public ResponseEntity<?> deleteAllScheduledMessages() {
        scheduledMessageService.deleteAllScheduledMessages();
        log.trace("DELETE /scheduled-messages called");
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update scheduled message",
            description = "Replaces old entity with the entity created from provided details, retaining the same ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @PutMapping(ScheduledMessagePath.ROOT + "/{id}")
    public ResponseEntity<?> updateScheduledMessage(
            @Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
            @PathVariable("id") Long messageId,
            @Parameter(name = "scheduled message details", description = "ScheduledMessageDetails object")
            @RequestBody ScheduledMessageDetails messageDetails) {
        scheduledMessageService.updateScheduledMessage(messageId, messageDetails);
        log.trace("PUT /scheduled-message/{id} called for entity with id: {}", messageId);
        return ResponseEntity.ok().build();
    }
}