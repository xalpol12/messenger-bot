package com.xalpol12.messengerbot.publisher.repository;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
}
