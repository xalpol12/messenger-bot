package com.xalpol12.messengerbot.crud.model.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Data Access Object created for use
 * in create and update operations on Image entity.
 */
@Schema(description = "Image input DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadDetails {

        @Nullable
        @NotBlank
        @Length(max = 80)
        @Schema(name = "Name",
                description = "New name that the Image entity will be updated with." +
                        "Must not be blank",
                example = "image2",
                nullable = true,
                maxLength = 80)
        String name;

        @Nullable
        @NotBlank
        @Length(max = 30)
        @Schema(name = "Custom URI",
                description = "New custom URI that the Image entity will be updated with, must be unique, " +
                        "not blank, kebab-case",
                example = "new-custom-uri",
                nullable = true,
                maxLength = 30)
        String customUri;
}

