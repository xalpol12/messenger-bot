package com.xalpol12.messengerbot.crud.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "scheduled_messages")
public class ScheduledMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "scheduled_message_id")
    private Long id;

    @NonNull
    @Future
    private LocalDateTime scheduledDate;

    @Length(max = 300)
    private String message;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(columnDefinition = "boolean default false")
    private boolean sent;
}