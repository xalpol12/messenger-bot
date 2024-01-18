package com.xalpol12.messengerbot.crud.controller.docs;

import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Scheduled Messages API", description = "API for scheduling messages to be sent through Messenger")
public interface IScheduledMessageController {

    class ScheduledMessagePath {
        public static final String ROOT = "/api/scheduled-message";
        private ScheduledMessagePath() {}
    }

    @Operation(
            summary = "Return scheduled message",
            description = "Returns scheduled message details in form of ScheduledMessageDTO based on provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @GetMapping(ScheduledMessagePath.ROOT + "/{id}")
     ResponseEntity<ScheduledMessageDTO> getScheduledMessage(
            @Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
            @PathVariable("id") Long messageId);

    @Operation(
            summary = "Return all scheduled messages",
            description = "Returns all scheduled message details in form of ScheduledMessageDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all entities from the database"),
    })
    @GetMapping(ScheduledMessagePath.ROOT + "s")
     ResponseEntity<List<ScheduledMessageDTO>> getAllScheduledMessages();

    @Operation(
            summary = "Upload scheduled message",
            description = "Saves new ScheduledMessage entity in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved new entity in the database"),
    }) // TODO: check other exceptions in this endpoint
    @PostMapping(ScheduledMessagePath.ROOT)
    ResponseEntity<ScheduledMessageDTO> uploadScheduledMessage(@Parameter(name = "scheduled message details", description = "ScheduledMessageDetails object")
                                                               @RequestBody ScheduledMessageDetails scheduledMessageDetails);

    @Operation(
            summary = "Delete scheduled message",
            description = "Deletes scheduled message with given ID from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @DeleteMapping(ScheduledMessagePath.ROOT + "/{id}")
    ResponseEntity<?> deleteScheduledMessage(@Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
                                             @PathVariable("id") Long messageId);

    @Operation(
            summary = "Delete selected scheduled messages",
            description = "Deletes scheduled messages with IDs passed to this endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted ScheduledMessage entities with given IDs from the database"),
    })
    @DeleteMapping(ScheduledMessagePath.ROOT + "s" + "/batch")
    ResponseEntity<?> deleteSelectedScheduledMessages(@Parameter(name = "messageIds", description = "List of message IDs for deletion")
                                                      @RequestBody List<Long> messageIds);

    @Operation(
            summary = "Delete all scheduled message",
            description = "Deletes all scheduled messages from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the ScheduledMessage entity from the database"),
    })
    @DeleteMapping(ScheduledMessagePath.ROOT + "s")
    ResponseEntity<?> deleteAllScheduledMessages();

    @Operation(
            summary = "Update scheduled message",
            description = "Replaces old entity with the entity created from provided details, retaining the same ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the ScheduledMessage entity from the database"),
            @ApiResponse(responseCode = "404", description = "ScheduledMessage with given ID not found in the database"),
    })
    @PutMapping(ScheduledMessagePath.ROOT + "/{id}")
    ResponseEntity<?> updateScheduledMessage(@Parameter(name = "id", description = "Unique ScheduledMessage entity identifier")
                                             @PathVariable("id") Long messageId,
                                             @Parameter(name = "scheduled message details", description = "ScheduledMessageDetails object")
                                             @RequestBody ScheduledMessageDetails messageDetails);
}
