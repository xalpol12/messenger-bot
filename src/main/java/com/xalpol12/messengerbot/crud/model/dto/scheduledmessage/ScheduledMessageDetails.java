package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import lombok.Data;

@Schema(description = "Scheduled message input DTO")
@Data
public class ScheduledMessageDetails {

    @Schema(name = "Scheduled date",
            description = "LocalDateTime that the message should be published at (GMT+0), must be in the future",
            example = "2024-01-03T12:26:00")
    private String scheduledDate;

    @Schema(name = "Message",
            description = "Actual message content that will be published",
            example = "Hello! This is an example.",
            maxLength = 300)
    private String message;

    @Nullable
    @Schema(name = "Image ID",
            description = "Image ID to create one-to-many relation between " +
                    "ScheduledMessage and Image",
            example = "558e89c8-79d2-4312-9750-73c24afe253e",
            nullable = true)
    private String imageId;
}
