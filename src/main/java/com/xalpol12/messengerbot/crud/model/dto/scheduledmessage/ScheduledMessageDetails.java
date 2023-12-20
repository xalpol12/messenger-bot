package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

import lombok.Data;

@Data
public class ScheduledMessageDetails {
    private String scheduledDate;
    private String message;
    private String imageId;
}
