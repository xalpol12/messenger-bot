package com.xalpol12.messengerbot.crud.model.dto.image;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadDetails {

        @Nullable
        String name;
        @Nullable
        String customUri;
}

