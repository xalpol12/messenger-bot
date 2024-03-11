package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ImageCachingTest {

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ScheduledMessageRepository messageRepository;
    @Mock
    private ThumbnailService thumbnailService;
    @Mock
    private ImageMapper mapper;
    private ImageService imageService;

    @Autowired
    private CacheManager cacheManager;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository, messageRepository, thumbnailService, mapper);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    @Disabled
    public void testCaching() {
        String cacheName = "imageCache";
        String imageId = "uuid";
        Image image = Image.builder().id(imageId).build();

        when(imageRepository.existsById(imageId)).thenReturn(true);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        Image firstImage = imageService.getImage(imageId);
        Image secondImage = imageService.getImage(imageId);

        assertEquals(firstImage, secondImage); // should be the same image - received from cache
        assertCacheSize(cacheName, 1);
    }

    private void assertCacheSize(String cacheName, long expectedSize) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache instanceof CaffeineCache caffeineCache) {
            @NonNegative long size = caffeineCache.getNativeCache().estimatedSize();
            System.out.println("Cache content: " + caffeineCache.getNativeCache().asMap());
            assertEquals(expectedSize, size);
        } else {
            throw new UnsupportedOperationException("Unsupported cache type");
        }
    }
}