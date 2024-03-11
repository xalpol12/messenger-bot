package com.xalpol12.messengerbot.crud.model.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Access Object for Image entity, created for
 * returning details about the Image without accessing
 * real image data.
 */
@Schema(description = "Image output DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

   @Schema(name = "imageId",
           description = "Unique Image entity identifier (UUID)",
           example = "58e89c8-79d2-4312-9750-73c24afe253e")
   private String imageId;

   @Schema(name = "name",
           description = "Filename that will be displayed when the file is downloaded",
           example = "image1")
   private String name;

   @Schema(name = "url",
           description = "URL for accessing the image",
           example = "http://localhost:8080/api/image/munch-test")
   private String url;

   @Schema(name = "type",
           description = "File extension",
           example = "image/jpeg")
   private String type;

   @Schema(name = "size",
           description = "How large the actual image data in bytes is",
           example = "118778")
   private long size;

   @Schema(name = "createdAt",
           description = "LocalDateTime assigned at the entity creation",
           example = "2023-12-31T20:49:28.570727",
           accessMode = Schema.AccessMode.READ_ONLY)
   private LocalDateTime createdAt;

   @Schema(name = "lastModifiedAt",
           description = "LocalDateTime overwritten each time the entity is modified, " +
                   "initial value is the same as \"createdAt\" field",
           example = "2023-12-31T20:49:28.570727")
   private LocalDateTime lastModifiedAt;
}
