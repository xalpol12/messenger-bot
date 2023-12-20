package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledMessageService {
    private final ScheduledMessageRepository scheduledMessageRepository;


}
