package com.xalpol12.messengerbot.crud.repository;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {
    List<ScheduledMessage> findAllByScheduledDateBetweenAndSentIsFalse(LocalDateTime start, LocalDateTime end);
    List<ScheduledMessage> findAllByImageEquals(Image image);
    List<ScheduledMessage> findAllByImageIsNotNull();

    @Modifying
    @Query("DELETE FROM scheduled_messages sm WHERE sm.image.id IN :imageIds")
    void deleteAllByImageIds(List<String> imageIds);
}
