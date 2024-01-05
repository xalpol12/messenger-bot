package com.xalpol12.messengerbot.messengerplatform.model;

import com.xalpol12.messengerbot.messengerplatform.model.composite.WebhookEntry;
import lombok.Data;

import java.util.List;

/**
 * Expected webhook page
 * object structure representation.
 */
@Data
public class Webhook {
    private String object;
    private List<WebhookEntry> entry;
}
