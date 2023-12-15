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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public Image getImage(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No image found for provided id:" + id));
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
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEntity.getId())
                .toUri();
        return location;
    }

    public void deleteImage(String id) throws EntityNotFoundException {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        };
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
