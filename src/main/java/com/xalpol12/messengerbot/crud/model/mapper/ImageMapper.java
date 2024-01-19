package com.xalpol12.messengerbot.crud.model.mapper;

import com.xalpol12.messengerbot.crud.controller.ImageController;
import com.xalpol12.messengerbot.crud.exception.ImageAccessException;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

/**
 * ModelMapper wrapper class that encapsulates
 * logic for mapping Image entity class to different
 * forms.
 */
@Component
@RequiredArgsConstructor
public class ImageMapper {

    private final ModelMapper mapper;

    /**
     * Constructs new Image class instance from
     * provided image file and details. Image name is set to
     * original filename if no name is provided in ImageUploadDetails.
     * Custom URI entity field is set to null if no custom URI is provided in
     * ImageUploadDetails.
     * @param details ImageUploadDetails that might specify new image name
     *                and custom URI path for new entity
     * @param file MultipartFile representing uploaded image file
     * @return Image entity
     * @throws IOException thrown when couldn't access MultipartFile bytes
     */
    public Image mapToImage(ImageUploadDetails details, MultipartFile file) throws IOException {
        String imageName = details.getName() != null ? details.getName() : extractFilenameWithoutExtension(file);
        Dimension imageDimension = getImageDimensions(file);
        return Image.builder()
                .customUri(details.getCustomUri())
                .name(imageName)
                .data(file.getBytes())
                .width(imageDimension.width)
                .height(imageDimension.height)
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

    private Dimension getImageDimensions(MultipartFile file) {
        String filename = file.getOriginalFilename();
        try (ImageInputStream stream = ImageIO.createImageInputStream(file.getInputStream())) {
            if (stream != null) {
                IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
                Iterator<ImageReaderSpi> iter = iioRegistry.getServiceProviders(ImageReaderSpi.class, true);
                while (iter.hasNext()) {
                    ImageReaderSpi readerSpi = iter.next();
                    if (readerSpi.canDecodeInput(stream)) {
                        ImageReader reader = readerSpi.createReaderInstance();
                        try {
                            reader.setInput(stream);
                            int width = reader.getWidth(reader.getMinIndex());
                            int height = reader.getHeight(reader.getMinIndex());
                            return new Dimension(width, height);
                        } finally {
                            reader.dispose();
                        }
                    }
                }
                throw new IllegalArgumentException("Can't find decoder for image " + filename);
            } else {
                throw new IllegalArgumentException("Can't open stream for image " + filename);
            }
        } catch (IOException e) {
            throw new ImageAccessException(e.getMessage());
        }
    }

    /**
     * Constructs ImageDTO from Image entity.
     * URL field is constructed from current context path.
     * If provided Image entity does not have custom URI field set,
     * URL is constructed from ID.
     * @param image Image entity instance
     * @return ImageDTO instance based on provided Image entity
     */
    public ImageDTO mapToImageDTO(Image image) {
        String uriOrId = image.getCustomUri() != null ? image.getCustomUri() : image.getId();
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(ImageController.ImagePath.ROOT)
                .path("/" + uriOrId)
                .toUriString();

        return ImageDTO.builder()
                .imageId(image.getId())
                .name(image.getName())
                .url(imageUrl)
                .type(image.getType())
                .size(image.getData().length)
                .createdAt(image.getCreatedAt())
                .lastModifiedAt(image.getModifiedAt())
                .build();
    }


    /**
     * Constructs ImageDTO from Image entity.
     * Method used when ImageMapper is invoked outside the controller
     * context, then no explicit context path can be provided.
     * URL field is constructed from provided baseUrl String.
     * If provided Image entity does not have custom URI field set,
     * URL is constructed from ID.
     * @param image Image entity instance
     * @param baseUrl String determining the base server path
     * @return ImageDTO instance based on provided Image entity
     */
    public ImageDTO mapToImageDTO(Image image, String baseUrl) {
        String uriOrId = image.getCustomUri() != null ? image.getCustomUri() : image.getId();
        String imageUrl = baseUrl +
                ImageController.ImagePath.ROOT +
                "/" + uriOrId;

        return ImageDTO.builder()
                .imageId(image.getId())
                .name(image.getName())
                .url(imageUrl)
                .type(image.getType())
                .size(image.getData().length)
                .createdAt(image.getCreatedAt())
                .lastModifiedAt(image.getModifiedAt())
                .build();
    }

    /**
     * Maps Image with another Image changes.
     * @param source Image entity
     * @param destination Image entity
     */
    public void updateImage(Image source, Image destination) {
        mapper.map(source, destination);
    }

    /**
     * Maps Image with ImageUploadDetails changes.
     * @param image Image entity
     * @param details ImageUploadDetails instance
     */
    public void updateImageDetails(Image image, ImageUploadDetails details) {
        mapper.map(details, image);
    }

    /**
     * Sets new data Image entity field.
     * @param image Image entity
     * @param file MultipartFile image
     * @throws IOException thrown when couldn't access MultipartFile bytes
     */
    public void updateImageData(Image image, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        image.setData(data);
    }
}
