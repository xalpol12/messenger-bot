package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduledMessageController {

    private final ScheduledMessageService scheduledMessageService;


}
