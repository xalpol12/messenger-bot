package com.xalpol12.messengerbot.publisher.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing "subscribers"
 * table structure in the database.
 */
@Data
@Entity(name = "subscribers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {
    @Id
    private String userId;
}
