package com.xalpol12.messengerbot.crud.model.dto.image;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ImageResponse (
        String id,
        String name,
        String url,
        String type,
        long size,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) { }
