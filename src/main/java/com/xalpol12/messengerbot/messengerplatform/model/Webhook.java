package com.xalpol12.messengerbot.messengerplatform.model;

import com.xalpol12.messengerbot.messengerplatform.model.composite.WebhookEntry;
import lombok.Data;

import java.util.List;

@Data
public class Webhook {
    private String object;
    private List<WebhookEntry> entry;
}
