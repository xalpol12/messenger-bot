package com.xalpol12.messengerbot.crud.model.mapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageResponse;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Component
public class ImageMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        String imageName = details.name() != null ? details.name() : file.getOriginalFilename();
        return Image.builder()
                .customUri(details.customUri())
                .name(imageName)
                .data(file.getBytes())
                .type(file.getContentType())
                .build();
    }

    public ImageResponse mapToImageResponse(Image image) {
        String uriOrId = image.getCustomUri() != null ? image.getCustomUri() : image.getId();
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/")
                .path("/image/")
                .path(uriOrId)
                .toUriString();

        return ImageResponse.builder()
                .name(image.getName())
                .url(fileDownloadUri)
                .type(image.getType())
                .size(image.getData().length)
                .build();
    }
    public Image mapUpdatedDetailsToImage(Image image, ImageUploadDetails details) throws JsonMappingException {
        mapper.updateValue(image, details);
        return image;
    }

    public Image mapUpdatedImageDataToImage(Image image, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        return mapper.updateValue(image, data);
    }
}
