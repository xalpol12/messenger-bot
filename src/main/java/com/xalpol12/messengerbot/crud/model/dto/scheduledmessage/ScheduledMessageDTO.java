package com.xalpol12.messengerbot.crud.model.dto.scheduledmessage;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledMessageDTO {
   private Long scheduledMessageId;
   private LocalDateTime scheduledDate;
   private String message;
   @Nullable
   private String imageUrl;
   private boolean isSent;
}
