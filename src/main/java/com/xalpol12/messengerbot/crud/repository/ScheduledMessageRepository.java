package com.xalpol12.messengerbot.crud.repository;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {
    List<ScheduledMessage> findAllByScheduledDateBetweenAndSentIsFalse(LocalDateTime start, LocalDateTime end);
}
