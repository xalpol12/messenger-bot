package com.xalpol12.messengerbot.crud.model.dto;

import lombok.Builder;

@Builder
public record ImageResponse (
        String name,
        String url,
        String type,
        long size
) { }
