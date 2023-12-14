package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.model.dto.ImageUploadRequest;
import com.xalpol12.messengerbot.crud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images")
    public ResponseEntity<?> getAllImageTitleList() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/images/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        return null;
    }

    @DeleteMapping("image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("image/{id}")
    public ResponseEntity<?> updateImage(@RequestBody ImageUploadRequest imageUploadRequest,
                                         @PathVariable("id") String imageId) {
        imageService.updateImage(imageUploadRequest, imageId);
        return ResponseEntity.ok().build();
    }
}
