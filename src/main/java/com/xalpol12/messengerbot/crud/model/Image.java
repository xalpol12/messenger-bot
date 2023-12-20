package com.xalpol12.messengerbot.crud.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "images")
@EntityListeners(AuditingEntityListener.class)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    private String id;

    @Nullable
    @Column(unique = true) // TODO: add catching exception from postgres
    @Length(max = 30)
    @Pattern(regexp = "^\\S+$", message = "Invalid URI format")
    private String customUri;

    private String name;

    private String type;

    @Lob
    @JdbcTypeCode(Types.LONGVARBINARY)
    private byte[] data;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "scheduled_messages", fetch = FetchType.LAZY)
    Set<ScheduledMessage> scheduledMessages;
}
