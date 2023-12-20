package com.xalpol12.messengerbot.crud.repository;

import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {
}
