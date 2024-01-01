package com.xalpol12.messengerbot.messengerplatform.model.dto;

import lombok.Builder;

@Builder
public record MessageParams(
        String recipient,
        String message,
        String messagingType,
        String accessToken
) {
}
