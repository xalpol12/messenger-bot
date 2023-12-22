package com.xalpol12.messengerbot.crud.model.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
   private String id;
   private String name;
   private String url;
   private String type;
   private long size;
   private LocalDateTime createdAt;
   private LocalDateTime lastModifiedAt;
}
