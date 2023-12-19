package com.xalpol12.messengerbot.crud.model.dto;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ImageResponse (
        String id,
        String name,
        String url,
        String type,
        long size,
        Timestamp createdAt,
        Timestamp lastModifiedAt
) { }
