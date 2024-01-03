package com.xalpol12.messengerbot.crud.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.xalpol12.messengerbot.crud.model.Image;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import com.xalpol12.messengerbot.crud.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(name = "Images API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    public static class ImagePath {
        public static final String ROOT = "/api/image";
        private ImagePath() {};
    }

    private final ImageService imageService;

    @Operation(
            summary = "",
            description = "",
            tags = {})
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = ""),
    })
    @GetMapping(ImagePath.ROOT + "/{uri}")
    public ResponseEntity<byte[]> displayImageData(
            @PathVariable("uri") @Parameter(name = "uri", description = "", examples = {}) String uri) {
        Image image = imageService.getImage(uri);

        MediaType mediaType = MediaType.parseMediaType(image.getType());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getData());
    }

    @GetMapping(ImagePath.ROOT + "/{uri}/download")
    public ResponseEntity<byte[]> getImageData(@PathVariable("uri") String uri) {
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

    @GetMapping(ImagePath.ROOT + "s")
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        log.trace("GET /images called");
        List<ImageDTO> images = imageService.getAllImages();
        log.trace("GET /images returned {} elements", images.size());
        return ResponseEntity.ok(images);
    }

    @PostMapping(path = ImagePath.ROOT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart ImageUploadDetails fileDetails,
                                         @RequestPart MultipartFile file) {
        URI savedLocation = imageService.uploadImage(fileDetails, file);
        log.trace("POST /images/upload called for file with URI: {}", savedLocation);
        return ResponseEntity.created(savedLocation).build();
    }

    @DeleteMapping(ImagePath.ROOT + "/{uri}")
    public ResponseEntity<?> deleteImage(@PathVariable("uri") String uri) {
        log.trace("DELETE image/{uri} called for entity with uri: {}", uri);
        imageService.deleteImage(uri);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = ImagePath.ROOT + "/{uri}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("uri") String uri,
                                         @RequestPart ImageUploadDetails fileDetails,
                                         @RequestPart MultipartFile file) {
        log.trace("PUT image/{uri} called for entity with uri: {}", uri);
        imageService.updateImage(uri, fileDetails, file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(ImagePath.ROOT + "/{uri}/details")
    public ResponseEntity<?> updateImageDetails(@PathVariable("uri") String uri,
                                                @RequestBody ImageUploadDetails newDetails) {
        log.trace("PATCH image/{uri} image details called for entity with uri: {}", uri);
        imageService.patchImageDetails(uri, newDetails);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(ImagePath.ROOT + "/{uri}/data")
    public ResponseEntity<?> updateImageData(@PathVariable("uri") String uri,
                                             @RequestPart MultipartFile file) {
        log.trace("PATCH image/{uri} image data called for entity with uri: {}", uri);
        imageService.patchImageData(uri, file);
        return ResponseEntity.ok().build();
    }
}
