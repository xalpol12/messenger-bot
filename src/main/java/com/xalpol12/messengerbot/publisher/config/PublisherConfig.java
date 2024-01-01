package com.xalpol12.messengerbot.publisher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PublisherConfig {

    private static final int MESSAGES_EXECUTOR_THREAD_POOL = 5;
    private static final int SUBSCRIBERS_EXECUTOR_THREAD_POOL = 5;

    @Bean
    public ExecutorService messagesExecutor() {
        return Executors.newFixedThreadPool(MESSAGES_EXECUTOR_THREAD_POOL);
    }

    @Bean
    public ExecutorService subscribersExecutor() {
        return Executors.newFixedThreadPool(SUBSCRIBERS_EXECUTOR_THREAD_POOL);
    }
}
