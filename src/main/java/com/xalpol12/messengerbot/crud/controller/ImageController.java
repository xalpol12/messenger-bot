package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.controller.docs.IImageController;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageBatchDeleteRequest;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController implements IImageController {

    private final ImageService imageService;

    public ResponseEntity<byte[]> getFullImageData(String uri) {
        Image image = imageService.getImage(uri);

        MediaType mediaType = MediaType.parseMediaType(image.getType());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getData());
    }

    public ResponseEntity<byte[]> getThumbnail(String uri, String width, String height) {
        byte[] thumbnail = imageService.getThumbnail(uri, width, height);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(thumbnail, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> redirectToImageDataDownload(String uri) {
        Image image = imageService.getImage(uri);
        MediaType mediaType = MediaType.parseMediaType(image.getType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment",
                image.getName() + "." + image.getType().split("/")[1]);

        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getData());
    }

    public ResponseEntity<ImageDTO> getImageInfo(String uri) {
        log.trace("GET /image/{}/details called", uri);
        ImageDTO imageDTO = imageService.getImageInfo(uri);
        return ResponseEntity.ok(imageDTO);
    }

    public ResponseEntity<List<ImageDTO>> getAllImageInfos() {
        log.trace("GET /images called");
        List<ImageDTO> images = imageService.getAllImageInfos();
        log.trace("GET /images returned {} elements", images.size());
        return ResponseEntity.ok(images);
    }

    public ResponseEntity<URI> uploadImage(ImageUploadDetails fileDetails, MultipartFile file) {
        URI savedLocation = imageService.uploadImage(fileDetails, file);
        log.trace("POST /images/upload called for file with URI: {}", savedLocation);
        return ResponseEntity.created(savedLocation).build();
    }

    public ResponseEntity<Void> deleteImage(String uri) {
        log.trace("DELETE image/{uri} called for entity with uri: {}", uri);
        imageService.deleteImage(uri);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteSelectedImages(ImageBatchDeleteRequest deleteRequest) {
        log.trace("PUT (DELETE) batch images called");
        imageService.deleteSelectedImages(deleteRequest);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteAllImages() {
        log.trace("DELETE /images called");
        imageService.deleteAllImages();
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> updateImage(String uri, ImageUploadDetails fileDetails, MultipartFile file) {
        log.trace("PUT image/{uri} called for entity with uri: {}", uri);
        imageService.updateImage(uri, fileDetails, file);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> updateImageDetails(String uri, ImageUploadDetails newDetails) {
        log.trace("PATCH image/{uri} image details called for entity with uri: {}", uri);
        imageService.patchImageDetails(uri, newDetails);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> updateImageData(String uri, MultipartFile file) {
        log.trace("PATCH image/{uri} image data called for entity with uri: {}", uri);
        imageService.patchImageData(uri, file);
        return ResponseEntity.ok().build();
    }
}
