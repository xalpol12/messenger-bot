package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.controller.ImageController;
import com.xalpol12.messengerbot.crud.exception.ImageAccessException;
import com.xalpol12.messengerbot.crud.exception.ImageNotFoundException;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.ScheduledMessage;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.model.mapper.ImageMapper;
import com.xalpol12.messengerbot.crud.repository.ImageRepository;
import com.xalpol12.messengerbot.crud.repository.ScheduledMessageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    private final ScheduledMessageRepository scheduledMessageRepository;
    private final ImageMapper imageMapper;

    @Cacheable("imageCache")
    public Image getImage(String id) {
        return findByCustomUriOrId(id);
    }

    private Image findByCustomUriOrId(String customUriOrId) {
        Image image;
        if (imageRepository.existsById(customUriOrId)) {
            image = imageRepository.findById(customUriOrId).get();
            log.warn("No custom uri found for entity with id: {}", customUriOrId);
        } else {
            image = imageRepository.findImageByCustomUri(customUriOrId)
                    .orElseThrow(() -> new ImageNotFoundException("No image found for: " + customUriOrId));
        }
        return image;
    }

    public List<ImageDTO> getAllImages() {
        Stream<Image> imageStream = imageRepository.findAll().stream();
        return imageStream
                .map(imageMapper::mapToImageDTO)
                .toList();
    }

    @Cacheable("imageCache")
    public URI uploadImage(ImageUploadDetails fileDetails,
                             MultipartFile imageData) throws ImageAccessException {
        Image newImage;
        try {
            newImage = imageMapper.mapToImage(fileDetails, imageData);
        } catch (IOException e) {
            throw new ImageAccessException("Could not access image with name:" + imageData.getOriginalFilename());
        }
        Image savedEntity = imageRepository.save(newImage);
        String uriOrId = savedEntity.getCustomUri() != null ? savedEntity.getCustomUri() : savedEntity.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(ImageController.ImagePath.ROOT + "/{id}")
                .buildAndExpand(uriOrId)
                .toUri();
        log.info("Successfully saved entity with identifier: {}", uriOrId);
        return location;
    }

    @Transactional
    public void deleteImage(String id) throws EntityNotFoundException {
        Image image = findByCustomUriOrId(id);
        List<ScheduledMessage> messages = scheduledMessageRepository.findAllByImageEquals(image);
        scheduledMessageRepository.deleteAll(messages);
        log.info("All {} scheduled messages associated with image {} " +
                "have been deleted", messages.size(), id);
        imageRepository.delete(image);
        log.info("Image with identifier: {} has been deleted", id);
    }


    @Transactional
    public void updateImage(String customUriOrId,
                            ImageUploadDetails fileDetails,
                            MultipartFile imageData) {
        Image originalImage = findByCustomUriOrId(customUriOrId);
        try {
            Image updatedImage = imageMapper.mapToImage(fileDetails, imageData);
            imageMapper.updateImage(updatedImage, originalImage);
            log.info("Updated entity with identifier: {}", customUriOrId);
        } catch (IOException e) {
            throw new ImageAccessException("Could not access image with name:" + imageData.getOriginalFilename());
        }
    }

    @Transactional
    public void patchImageDetails(String customUriOrId, ImageUploadDetails newDetails) {
        Image imageToPatch = findByCustomUriOrId(customUriOrId);
        imageMapper.updateImageDetails(imageToPatch, newDetails);
        log.info("Patched image details for entity with identifier: {}", customUriOrId);
    }

    @Transactional
    public void patchImageData(String customUriOrId, MultipartFile file) {
        Image imageToPatch = findByCustomUriOrId(customUriOrId);
        try {
            imageMapper.updateImageData(imageToPatch, file);
            log.info("Patched image data for entity with identifier: {}", customUriOrId);
        } catch (IOException e) {
            throw new ImageAccessException("Could not access image with name:" + file.getOriginalFilename());
        }
    }
}
