package com.xalpol12.messengerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MessengerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerBotApplication.class, args);
    }

}
