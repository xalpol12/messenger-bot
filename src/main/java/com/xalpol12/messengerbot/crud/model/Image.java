package com.xalpol12.messengerbot.crud.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Image {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", type = org.hibernate.id.uuid.UuidGenerator.class)
    @Column(name = "image_id")
    private String id;

    private String name;

    @Lob
    private byte[] data;

}
