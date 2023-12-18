package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageResponse;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ImageMapper {

    private final ModelMapper mapper;

    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        String imageName = details.getName() != null ? details.getName() : extractFilenameWithoutExtension(file);
        return Image.builder()
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
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/")
                .path("/image/")
                .path(uriOrId)
                .toUriString();

        return ImageResponse.builder()
                .id(image.getId())
                .name(image.getName())
                .url(imageUrl)
                .type(image.getType())
                .size(image.getData().length)
                .build();
    }

    public void updateImage(Image source, Image destination) {
        mapper.map(source, destination);
    }

    public void updateImageDetails(Image image, ImageUploadDetails details) {
        mapper.map(details, image);
    }

    public void updateImageData(Image image, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        image.setData(data);
    }
}
