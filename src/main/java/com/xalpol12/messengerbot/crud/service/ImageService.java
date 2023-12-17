package com.xalpol12.messengerbot.crud.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.ImageResponse;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public Image getImage(String id) {
        return findByCustomUriOrId(id);
    }

    public List<ImageResponse> getAllImages() {
        Stream<Image> imageStream = imageRepository.findAll().stream();
        return imageStream
                .map(imageMapper::mapToImageResponse)
                .toList();
    }

    public URI uploadImage(ImageUploadDetails fileDetails,
                             MultipartFile imageData) throws IOException {
        Image newImage = imageMapper.mapToImage(fileDetails, imageData);
        Image savedEntity = imageRepository.save(newImage);
        String uriOrId = savedEntity.getCustomUri() != null ? savedEntity.getCustomUri() : savedEntity.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uriOrId)
                .toUri();
        return location;
    }

    public void deleteImage(String id) throws EntityNotFoundException {
        Image image = findByCustomUriOrId(id);
        imageRepository.deleteById(image.getId());
    }

    private Image findByCustomUriOrId(String customUriOrId) {
        Image image;
        if (imageRepository.existsById(customUriOrId)) {
            image = imageRepository.findById(customUriOrId).get();
        } else {
            image = imageRepository.findImageByCustomUri(customUriOrId)
                    .orElseThrow(() -> new EntityNotFoundException("No image found for: " + customUriOrId));
        }
        return image;
    }

    @Transactional
    public void updateImage(String id,
                            ImageUploadDetails fileDetails,
                            MultipartFile imageData) throws IOException {
        deleteImage(id);
        Image updatedImage = imageMapper.mapToImage(fileDetails, imageData);
        imageRepository.save(updatedImage);
    }

    @Transactional
    public void patchImageDetails(String id, ImageUploadDetails newDetails) throws JsonMappingException {
        Image imageToPatch = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No image found for this id:" + id));
        imageMapper.mapUpdatedDetailsToImage(imageToPatch, newDetails);
    }

    public void patchImageData(String id, MultipartFile file) throws IOException {
        Image imageToPatch = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No image found for this id:" + id));
        imageMapper.mapUpdatedImageDataToImage(imageToPatch, file);
    }
}
