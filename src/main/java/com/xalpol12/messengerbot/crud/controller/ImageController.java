package com.xalpol12.messengerbot.crud.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.xalpol12.messengerbot.crud.model.dto.ImageResponse;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("image/{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable("id") String imageId) {
        ImageResponse image = imageService.getImage(imageId);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/images")
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        log.trace("GET /images called");
        List<ImageResponse> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @PostMapping(path = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart ImageUploadDetails fileDetails,
                                         @RequestPart MultipartFile file) throws IOException {
        URI savedLocation = imageService.uploadImage(fileDetails, file);
        log.trace("POST /images/upload called for file with URI: {}", savedLocation);
        return ResponseEntity.created(savedLocation).build();
    }

    @DeleteMapping("image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String imageId) {
        log.trace("DELETE image/{id} called for entity with id: {}", imageId);
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("id") String imageId,
                                         @RequestPart ImageUploadDetails fileDetails,
                                         @RequestPart MultipartFile file) throws IOException {
        imageService.updateImage(imageId, fileDetails, file);
        log.trace("PUT image/{id} called for entity with id: {}", imageId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("image/{id}/details")
    public ResponseEntity<?> updateImageDetails(@PathVariable("id") String imageId,
                                                @RequestBody ImageUploadDetails newDetails) throws JsonMappingException {
        log.trace("PATCH image/{id} image details called for entity with id: {}", imageId);
        imageService.patchImageDetails(imageId, newDetails);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("image/{id}/data")
    public ResponseEntity<?> updateImageData(@PathVariable("id") String imageId,
                                             @RequestPart MultipartFile file) throws IOException {
        log.trace("PATCH image/{id} image data called for entity with id: {}", imageId);
        imageService.patchImageData(imageId, file);
        return ResponseEntity.ok().build();
    }
}
