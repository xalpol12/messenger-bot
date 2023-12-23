package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.config.ProjectConfig;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ImageMapperUnitTest {

    private static ImageMapper imageMapper;

    @BeforeAll
    public static void setup() {
        ProjectConfig config = new ProjectConfig(); // use current project config beans
        ModelMapper mapper = config.modelMapper(); // get instance of ModelMapper with current settings
        imageMapper = new ImageMapper(mapper);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
        Image image = Image.builder()
                .id("id")
                .customUri("custom-uri")
                .name("name")
                .type("image/jpeg")
                .data(new byte[0])
                .createdAt(LocalDateTime.MIN)
                .modifiedAt(LocalDateTime.MIN)
                .scheduledMessages(null)
                .build();

        ImageDTO result = imageMapper.mapToImageDTO(image);

        assertAll(() -> {
            assertEquals(image.getId(), result.getId());
            assertEquals(image.getName(), result.getName());
            assertEquals("http://localhost/api/image/" + image.getCustomUri(), result.getUrl());
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
            assertEquals(image.getId(), result.getId());
            assertEquals(image.getName(), result.getName());
            assertEquals("http://localhost/api/image/" + image.getId(), result.getUrl());
            assertEquals(image.getType(), result.getType());
            assertEquals(image.getData().length, result.getSize());
            assertEquals(image.getCreatedAt(), result.getCreatedAt());
            assertEquals(image.getModifiedAt(), result.getLastModifiedAt());
        });
    }
}