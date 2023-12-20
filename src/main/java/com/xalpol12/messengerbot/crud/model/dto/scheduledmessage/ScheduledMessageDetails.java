package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

public record ScheduledMessageDetails(
        String scheduledDate,
        String message,
        String imageId
) { }
