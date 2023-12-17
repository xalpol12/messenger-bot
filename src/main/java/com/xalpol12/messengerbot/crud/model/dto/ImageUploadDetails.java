package com.xalpol12.messengerbot.crud.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class ImageUploadDetails {

        @Nullable
        String name;
        @Nullable
        String customUri;
}

