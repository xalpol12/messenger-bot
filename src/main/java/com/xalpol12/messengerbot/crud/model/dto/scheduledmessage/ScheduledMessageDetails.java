package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Access Object created for use
 * in create and update operations on ScheduledMessage entity.
 */
@Schema(description = "Scheduled message input DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduledMessageDetails {

    @Schema(name = "scheduledDate",
            description = "LocalDateTime that the message should be published at (GMT+0), must be in the future",
            example = "2025-01-03T12:26:00")
    private String scheduledDate;

    @Schema(name = "message",
            description = "Actual message content that will be published",
            example = "Hello! This is an example.",
            maxLength = 300)
    private String message;

    @Nullable
    @Schema(name = "imageId",
            description = "Image ID to create one-to-many relation between " +
                    "ScheduledMessage and Image. Image with given ID must exist in " +
                    "the database",
            example = "558e89c8-79d2-4312-9750-73c24afe253e",
            nullable = true)
    private String imageId;
}
