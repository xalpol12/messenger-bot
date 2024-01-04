package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Scheduled message output DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledMessageDTO {

   @Schema(name = "Scheduled message ID",
           description = "Unique ScheduledMessage entity identifier (long)",
           example = "1352")
   private Long scheduledMessageId;

   @Schema(name = "Scheduled date",
           description = "LocalDateTime that the message should be published at",
           example = "2024-01-03T12:26:00")
   private LocalDateTime scheduledDate;

   @Schema(name = "Message",
           description = "Actual message content that will be published",
           example = "Hello! This is an example.",
           maxLength = 300)
   private String message;

   @Nullable
   @Schema(name = "Image URL",
           description = "Link to an image that will be appended to the actual image " +
                   "and published. Null if ScheduledMessage is not associated with any image.",
           example = "http://localhost:8080/api/image/munch-showcase")
   private String imageUrl;

   @Schema(name = "Is sent",
           description = "Scheduled message status, determines if scheduled message " +
                   "has been successfully published",
           example = "false")
   private boolean isSent;
}
