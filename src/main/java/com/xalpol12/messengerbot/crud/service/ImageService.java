package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageConfirmationDTO;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadRequest;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public Image getImage(String id) {
        Image image = imageRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);
        return image;
    }

    public Stream<Image> getAllImages() {
        return imageRepository.findAll().stream();
    }

    public ImageConfirmationDTO store(ImageUploadRequest imageUploadRequest) {
        Image image = modelMapper.map(imageUploadRequest);
        Image uploadedImage = imageRepository.save(image);
        return modelMapper.map(uploadedImage);
    }

    public void deleteImage(String id) {
        imageRepository.deleteById(id);
    }

    public void updateImage(ImageUploadRequest imageUploadRequest,
                            String id) {
        imageRepository.deleteById(id);
        Image image = modelMapper.map(imageUploadRequest);
        imageRepository.save(image);
    }
}
