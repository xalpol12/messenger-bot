package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.controller.ImageController;
import com.xalpol12.messengerbot.crud.exception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private static ImageRepository imageRepository;
    @Mock
    private static ScheduledMessageRepository messageRepository;
    @Mock
    private static ImageMapper imageMapper;

    private static ImageService imageService;

    private AutoCloseable openMocks;

    @BeforeAll
    public static void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository, messageRepository, imageMapper);
    }

    @AfterEach
    public void teardown() throws Exception {
        validateMockitoUsage();
        openMocks.close();
    }

    @Test
    public void getImage_shouldReturnImage_imageIdProvided() {
        String imageId = "image-id";

        when(imageRepository.existsById(imageId)).thenReturn(true);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(new Image()));

        Image result = imageService.getImage(imageId);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals(result.getClass(), Image.class);
            verify(imageRepository, times(1)).existsById(imageId);
            verify(imageRepository, times(1)).findById(imageId);
        });
    }

    @Test
    public void getImage_shouldReturnImage_imageCustomUriProvided() {
        String imageId = "image-custom-uri";

        when(imageRepository.existsById(imageId)).thenReturn(false);
        when(imageRepository.findImageByCustomUri(imageId)).thenReturn(Optional.of(new Image()));

        Image result = imageService.getImage(imageId);

        assertAll(() -> {
            assertNotNull(result);
            assertEquals(result.getClass(), Image.class);
            verify(imageRepository, times(1)).existsById(imageId);
            verify(imageRepository, times(1)).findImageByCustomUri(imageId);
        });
    }

    @Test
    public void getImage_shouldThrowEntityNotFoundException_notExistingIdProvided() {
        String imageId = "not-existing";

        when(imageRepository.existsById(imageId)).thenReturn(false);
        when(imageRepository.findImageByCustomUri(imageId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.getImage(imageId));
    }

    @Test
    public void getAllImages_shouldReturnListOfImageDTO() {
        when(imageRepository.findAll()).thenReturn(List.of(new Image(), new Image()));
        when(imageMapper.mapToImageDTO(any())).thenReturn(new ImageDTO());

        List<ImageDTO> results = imageService.getAllImages();

        assertAll(() -> {
            verify(imageMapper, times(2)).mapToImageDTO(any());
            assertEquals(2, results.size());
        });
    }

    @Test
    public void uploadImage_shouldReturnAccessPath_customUriProvided() throws IOException {
        String customUri = "custom-uri";
        ImageUploadDetails details = ImageUploadDetails.builder()
                .customUri(customUri)
                .build();
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);
        Image expectedImage = Image.builder()
                .customUri(details.getCustomUri())
                .build();
        URI expectedPath = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ImageController.ImagePath.ROOT)
                .path("/{id}")
                .buildAndExpand(customUri).toUri();

        when(imageMapper.mapToImage(details, file)).thenReturn(expectedImage);
        when(imageRepository.save(any())).thenReturn(expectedImage);

        URI receivedPath = imageService.uploadImage(details, file);

        assertAll(() -> {
            verify(imageMapper).mapToImage(details, file);
            assertEquals(expectedPath, receivedPath);
        });
    }

    @Test
    public void uploadImage_shouldReturnAccessPath_noCustomUriAndNameProvided() throws IOException {
        ImageUploadDetails details = ImageUploadDetails.builder()
                .customUri(null)
                .build();
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);
        String imageId = "image-id";
        Image expectedImage = Image.builder()
                .id(imageId)
                .customUri(details.getCustomUri())
                .build();
        URI expectedPath = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ImageController.ImagePath.ROOT)
                .path("/{id}")
                .buildAndExpand(imageId).toUri();

        when(imageMapper.mapToImage(details, file)).thenReturn(expectedImage);
        when(imageRepository.save(any())).thenReturn(expectedImage);

        URI receivedPath = imageService.uploadImage(details, file);

        assertAll(() -> {
            verify(imageMapper, times(1)).mapToImage(details, file);
            verify(imageRepository, times(1)).save(any());
            assertEquals(expectedPath, receivedPath);
        });
    }

    @Test
    public void deleteImage_shouldCallDeleteByIdMethod() {
        String id = "id";
        Image image = mock(Image.class);
        ScheduledMessage message = mock(ScheduledMessage.class);
        List<ScheduledMessage> messages = List.of(message);

        when(imageRepository.existsById(id)).thenReturn(true);
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        when(messageRepository.findAllByImageEquals(image)).thenReturn(messages);

        imageService.deleteImage(id);

        verify(messageRepository, times(1)).findAllByImageEquals(image);
        verify(messageRepository, times(1)).deleteAllInBatch(messages);
        verify(imageRepository, times(1)).delete(image);
    }


    @Test
    public void deleteAllImages_shouldCallDeleteAll() {
        ScheduledMessage message = mock(ScheduledMessage.class);
        List<ScheduledMessage> messages = List.of(message);

        when(messageRepository.findAllByImageIsNotNull()).thenReturn(messages);

        imageService.deleteAllImages();

        verify(messageRepository, times(1)).findAllByImageIsNotNull();
        verify(messageRepository, times(1)).deleteAllInBatch(messages);
        verify(imageRepository, times(1)).deleteAllInBatch();
    }

    @Test
    public void updateImage_shouldCallImageMapperUpdateMethod() throws IOException {
        String id = "id";
        Image originalImage = new Image();
        originalImage.setId(id);
        Image updatedImage = new Image();
        ImageUploadDetails details = new ImageUploadDetails();
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);


        when(imageRepository.existsById(id)).thenReturn(true);
        when(imageRepository.findById(id)).thenReturn(Optional.of(originalImage));
        when(imageMapper.mapToImage(details, file)).thenReturn(updatedImage);

        imageService.updateImage(id, details, file);

        verify(imageMapper, times(1)).updateImage(updatedImage, originalImage);
    }

    @Test
    public void patchImageDetails_shouldCallImageMapperPatchImageDetailsMethod() {
        String id = "id";
        Image originalImage = new Image();
        originalImage.setId(id);
        ImageUploadDetails details = new ImageUploadDetails();

        when(imageRepository.existsById(id)).thenReturn(true);
        when(imageRepository.findById(id)).thenReturn(Optional.of(originalImage));

        imageService.patchImageDetails(id, details);

        verify(imageMapper, times(1)).updateImageDetails(originalImage, details);
    }

    @Test
    public void patchImageData_shouldCallImageMapperPatchImageDataMethod() throws IOException {
        String id = "id";
        Image originalImage = new Image();
        originalImage.setId(id);
        MultipartFile file = new MockMultipartFile("filename", new byte[0]);

        when(imageRepository.existsById(id)).thenReturn(true);
        when(imageRepository.findById(id)).thenReturn(Optional.of(originalImage));

        imageService.patchImageData(id, file);

        verify(imageMapper, times(1)).updateImageData(originalImage, file);
    }
}