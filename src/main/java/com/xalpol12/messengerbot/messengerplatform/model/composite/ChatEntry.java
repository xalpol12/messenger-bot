package com.xalpol12.messengerbot.messengerplatform.model.composite;

import com.xalpol12.messengerbot.messengerplatform.model.detail.Message;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import lombok.Data;

import java.time.Instant;

@Data
public class ChatEntry {
    private Subject sender;
    private Subject recipient;
    private Instant timestamp;
    private Message message;
}
