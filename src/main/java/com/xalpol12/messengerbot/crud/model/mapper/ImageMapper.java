package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ImageMapper {
    private final ModelMapper mapper = new ModelMapper();

    ImageMapper() {
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        return Image.builder()
                .name(details.name())
                .data(file.getBytes())
                .type(file.getContentType())
                .build();
    }

    public Image mapUpdatedFieldsToImage(Image image, ImageUploadDetails details) {
    }
}
