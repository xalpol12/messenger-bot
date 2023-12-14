package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageConfirmationDTO;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public Image getImage(String id) {
        Image image = imageRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);
        return image;
    }

    public Stream<Image> getAllImages() {
        return imageRepository.findAll().stream();
    }

    public Image uploadImage(ImageUploadDetails fileDetails,
                                            MultipartFile imageData) throws IOException {
        Image image = imageMapper.mapToImage(fileDetails, imageData);
        return imageRepository.save(image);
    }

    public void deleteImage(String id) throws EntityNotFoundException {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        };
    }

    public void updateImage(String id,
                            ImageUploadDetails fileDetails,
                            MultipartFile imageData) throws IOException {
        deleteImage(id);
        Image updatedImage = imageMapper.mapToImage(fileDetails, imageData);
        imageRepository.save(updatedImage);
    }

    public void updateImageDetails(String id, ImageUploadDetails newDetails) {
    }
}
