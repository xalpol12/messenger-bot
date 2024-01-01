package com.xalpol12.messengerbot.messengerplatform.model.dto;

import com.xalpol12.messengerbot.messengerplatform.model.detail.Message;
import com.xalpol12.messengerbot.messengerplatform.model.detail.Subject;
import lombok.Builder;

@Builder
public record MessageParams(
        String recipient,
        String message,
        String messagingType,
        String accessToken
) {
}
