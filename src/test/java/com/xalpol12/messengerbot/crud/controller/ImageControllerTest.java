package com.xalpol12.messengerbot.crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.service.ImageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    public void teardown() {
        validateMockitoUsage();
    }

    @Test
    public void displayImageData_returnImageData_expect200() throws Exception {
        String uri = "uri";
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri;
        Image responseImage = getSampleImage();

        when(imageService.getImage(uri)).thenReturn(responseImage);

        this.mockMvc
                .perform(get(endpointPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(responseImage.getData()));
    }

    @Test
    public void getImageData_returnAttachment_expect200() throws Exception {
        String uri = "uri";
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri + "/download";
        Image responseImage = getSampleImage();

        when(imageService.getImage(uri)).thenReturn(responseImage);

        this.mockMvc
                .perform(get(endpointPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "form-data; name=\"attachment\"; filename=\"" + responseImage.getName() + "." + responseImage.getType().split("/")[1] + "\""))
                .andExpect(content().bytes(responseImage.getData()));

        verify(imageService, times(1)).getImage(uri);
    }

    @Test
    public void getAllImages_returnListOfImages_expect200() throws Exception {
        String endpointPath = ImageController.ImagePath.ROOT + "s";
        List<ImageDTO> responseImages = List.of(getSampleImageDTO(), getSampleImageDTO());

        when(imageService.getAllImages()).thenReturn(responseImages);

        this.mockMvc
                .perform(get(endpointPath))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(responseImages)));

        verify(imageService, times(1)).getAllImages();
    }

    @Test
    public void uploadImage_returnCreatedURL_expect201() throws Exception {
        String endpointPath = ImageController.ImagePath.ROOT;
        ImageUploadDetails fileDetails = ImageUploadDetails.builder().build();
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(ImageController.ImagePath.ROOT + "/{id}")
                .buildAndExpand("image-uuid")
                .toUri();

        when(imageService.uploadImage(fileDetails, file)).thenReturn(location);

        this.mockMvc
                .perform((MockMvcRequestBuilders.multipart(endpointPath)
                        .file(file)
                        .file(new MockMultipartFile("fileDetails", "", "application/json", asJsonString(fileDetails).getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", location.toString()));

        verify(imageService, times(1)).uploadImage(fileDetails, file);
    }

    @Test
    public void deleteImage_expect204() throws Exception {
        String uri = "uri";
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri;

        this.mockMvc
                .perform(delete(endpointPath))
                .andExpect(status().isNoContent());

        verify(imageService, times(1)).deleteImage(uri);
    }


    @Test
    public void deleteAllImages_expect204() throws Exception {
        String endpointPath = ImageController.ImagePath.ROOT + "s";

        this.mockMvc
                .perform(delete(endpointPath))
                .andExpect(status().isNoContent());

        verify(imageService, times(1)).deleteAllImages();
    }

    @Test
    public void updateImage_expect200() throws Exception {
        String uri = "uri";
        ImageUploadDetails details = ImageUploadDetails.builder().build();
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri;
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(HttpMethod.PUT, endpointPath);

        this.mockMvc
                .perform(multipartRequest
                        .file(file)
                        .file(new MockMultipartFile("fileDetails", "", "application/json", asJsonString(details).getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).updateImage(uri, details, file);
    }

    @Test
    public void updateImageDetails_expect200() throws Exception {
        String uri = "uri";
        ImageUploadDetails details = ImageUploadDetails.builder().build();
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri + "/details";

        this.mockMvc
                .perform(patch(endpointPath)
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(imageService, times(1)).patchImageDetails(uri, details);
    }

    @Test
    public void updateImageData_expect200() throws Exception {
        String uri = "uri";
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);
        String endpointPath = ImageController.ImagePath.ROOT + "/" + uri + "/data";
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(HttpMethod.PATCH, endpointPath);

        this.mockMvc
                .perform(multipartRequest.file(file))
                .andExpect(status().isOk());

        verify(imageService, times(1)).patchImageData(uri, file);
    }

    private Image getSampleImage() {
        byte[] expectedData = new byte[] { 1 };
        return Image.builder()
                .id("image-uuid")
                .name("mock-image")
                .type("image/jpeg")
                .data(expectedData)
                .createdAt(LocalDateTime.MIN)
                .modifiedAt(LocalDateTime.MIN)
                .build();
    }

    private ImageDTO getSampleImageDTO() {
        return ImageDTO.builder()
                .imageId("image-uuid")
                .name("mock-image")
                .type("image/jpeg")
                .build();
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}