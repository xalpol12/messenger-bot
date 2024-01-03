package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.controller.ImageController;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageMapperTest {

    @Mock
    private ModelMapper modelMapper;

    private ImageMapper imageMapper;

    private AutoCloseable openMocks;

    @BeforeAll
    public static void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        imageMapper = new ImageMapper(modelMapper);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void mapToImage_shouldReturnImage_allDetailsProvided() throws IOException {
        ImageUploadDetails details = ImageUploadDetails.builder()
                .name("custom-name-provided")
                .customUri("custom-uri-provided")
                .build();
        MultipartFile file = new MockMultipartFile("name", "original_filename.jpg",
                "sample-content-type", new byte[0]);

        Image result = imageMapper.mapToImage(details, file);

        assertAll(() -> {
            assertEquals(details.getCustomUri(), result.getCustomUri());
            assertEquals(details.getName(), result.getName());
            assertEquals(file.getBytes(), result.getData());
            assertEquals(file.getContentType(), result.getType());
        });
    }

    @Test
    public void mapToImage_shouldReturnImage_noCustomNameProvided() throws IOException {
        ImageUploadDetails details = ImageUploadDetails.builder()
                .name(null)
                .customUri("custom-uri-provided")
                .build();
        MultipartFile file = new MockMultipartFile("name", "original_filename.jpg",
                "sample-content-type", new byte[0]);

        Image result = imageMapper.mapToImage(details, file);

        assertAll(() -> {
            assertEquals(details.getCustomUri(), result.getCustomUri());
            assertEquals("original_filename", result.getName());
            assertEquals(file.getBytes(), result.getData());
            assertEquals(file.getContentType(), result.getType());
        });
    }

    @Test
    public void mapToImage_shouldReturnImage_noCustomUriProvided() throws IOException {
        ImageUploadDetails details = ImageUploadDetails.builder()
                .name("custom-name-provided")
                .customUri(null)
                .build();
        MultipartFile file = new MockMultipartFile("name", "original_filename.jpg",
                "sample-content-type", new byte[0]);

        Image result = imageMapper.mapToImage(details, file);

        assertAll(() -> {
            assertNull(result.getCustomUri());
            assertEquals(details.getName(), result.getName());
            assertEquals(file.getBytes(), result.getData());
            assertEquals(file.getContentType(), result.getType());
        });
    }

    @Test
    public void mapToImageDTO_shouldReturnImageDTO_customUriProvided() {
        String customUri = "custom-uri";
        URI expectedPath = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ImageController.ImagePath.ROOT)
                .path("/{id}")
                .buildAndExpand(customUri).toUri();
        Image image = Image.builder()
                .id("id")
                .customUri(customUri)
                .name("name")
                .type("image/jpeg")
                .data(new byte[0])
                .createdAt(LocalDateTime.MIN)
                .modifiedAt(LocalDateTime.MIN)
                .scheduledMessages(null)
                .build();

        ImageDTO result = imageMapper.mapToImageDTO(image);

        assertAll(() -> {
            assertEquals(image.getId(), result.getImageId());
            assertEquals(image.getName(), result.getName());
            assertEquals(expectedPath.toString(), result.getUrl());
            assertEquals(image.getType(), result.getType());
            assertEquals(image.getData().length, result.getSize());
            assertEquals(image.getCreatedAt(), result.getCreatedAt());
            assertEquals(image.getModifiedAt(), result.getLastModifiedAt());
        });
    }

    @Test
    public void mapToImageDTO_shouldReturnImageDTO_noCustomUriProvided() {
        Image image = Image.builder()
                .id("id")
                .name("name")
                .type("image/jpeg")
                .data(new byte[0])
                .createdAt(LocalDateTime.MIN)
                .modifiedAt(LocalDateTime.MIN)
                .scheduledMessages(null)
                .build();

        ImageDTO result = imageMapper.mapToImageDTO(image);

        assertAll(() -> {
            assertEquals(image.getId(), result.getImageId());
            assertEquals(image.getName(), result.getName());
            assertEquals("http://localhost/api/image/" + image.getId(), result.getUrl());
            assertEquals(image.getType(), result.getType());
            assertEquals(image.getData().length, result.getSize());
            assertEquals(image.getCreatedAt(), result.getCreatedAt());
            assertEquals(image.getModifiedAt(), result.getLastModifiedAt());
        });
    }

    @Test
    void updateImage_shouldMapSourceToDestination() {
        Image source = new Image();
        Image destination = new Image();

        imageMapper.updateImage(source, destination);

        verify(modelMapper, times(1)).map(source, destination);
    }

    @Test
    void updateImageDetails_shouldMapDetailsToImage() {
        Image image = new Image();
        ImageUploadDetails details = new ImageUploadDetails();

        imageMapper.updateImageDetails(image, details);

        verify(modelMapper, times(1)).map(details, image);
    }

    @Test
    void updateImageData_shouldSetDataInImage() throws IOException {
        Image image = new Image();
        byte[] data = "test data".getBytes();
        MultipartFile file = mock(MultipartFile.class);

        when(file.getBytes()).thenReturn(data);

        imageMapper.updateImageData(image, file);

        assertArrayEquals(data, image.getData());
    }
}