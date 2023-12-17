package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageResponse;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Component
public class ImageMapper {
    private final ModelMapper mapper;

    ImageMapper() {
        mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        String imageName = details.getName() != null ? details.getName() : extractFilenameWithoutExtension(file);
        return Image.builder()
                .customUri(details.getCustomUri())
                .name(imageName)
                .data(file.getBytes())
                .type(file.getContentType())
                .build();
    }

    public Image mapToImage(String id, ImageUploadDetails details, MultipartFile file) throws IOException {
        String imageName = details.getName() != null ? details.getName() : extractFilenameWithoutExtension(file);
        return Image.builder()
                .id(id)
                .customUri(details.getCustomUri())
                .name(imageName)
                .data(file.getBytes())
                .type(file.getContentType())
                .build();
    }

    private String extractFilenameWithoutExtension(MultipartFile file) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return originalFilename.substring(0, lastDotIndex);
        } else {
            return originalFilename;
        }
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

    public void updateImageDetails(Image image, ImageUploadDetails details) {
        mapper.map(details, image);
    }

    public void updateImageData(Image image, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        mapper.map(data, image);
    }
}
