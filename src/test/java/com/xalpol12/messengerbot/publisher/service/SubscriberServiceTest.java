package com.xalpol12.messengerbot.publisher.service;

import com.xalpol12.messengerbot.publisher.model.Subscriber;
import com.xalpol12.messengerbot.publisher.repository.SubscriberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    private SubscriberService subscriberService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        subscriberService = new SubscriberService(subscriberRepository);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void getAllSubscribers_shouldCallRepository() {
        subscriberService.getAllSubscribers();

        verify(subscriberRepository).findAll();
    }

    @Test
    public void addNewSubscribers_existsById_shouldNotCallRepository() {
        Set<String> senderIds = Set.of("id-1", "id-2");

        when(subscriberRepository.existsById(anyString())).thenReturn(true);

        subscriberService.addNewSubscribers(senderIds);

        verify(subscriberRepository, never()).save(any(Subscriber.class));
    }

    @Test
    public void addNewSubscribers_doesNotExistById_shouldCallRepository() {
        Set<String> senderIds = Set.of("not-existing-id-1", "not-existing-id-2");

        when(subscriberRepository.existsById(anyString())).thenReturn(false);

        subscriberService.addNewSubscribers(senderIds);

        verify(subscriberRepository, times(2)).save(any(Subscriber.class));
    }
}