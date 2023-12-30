package com.xalpol12.messengerbot.messengerplatform.model.composite;

import lombok.Data;

import java.util.List;

@Data
public class WebhookEntry {
    private String id;
    private long time;
    private List<ChatEntry> messaging;
}
