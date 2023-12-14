package com.xalpol12.messengerbot.crud.model.mapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ImageMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        return Image.builder()
                .name(details.name())
                .data(file.getBytes())
                .type(file.getContentType())
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
