package com.xalpol12.messengerbot.crud.model.dto.image;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploadDetails {

        @Nullable
        String name;
        @Nullable
        String customUri;
}

