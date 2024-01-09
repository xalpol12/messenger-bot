package com.xalpol12.messengerbot.crud.controller.docs;

import com.xalpol12.messengerbot.crud.model.dto.image.ImageDTO;
import com.xalpol12.messengerbot.crud.model.dto.image.ImageUploadDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Images API", description = "API for managing images")
public interface IImageController {

    class ImagePath {
        public static final String ROOT = "/api/image";
        private ImagePath() {}
    }

    @Operation(
            summary = "Display image data",
            description = "Sends the image body for display in web browser as a page. " +
                    "Caches accessed entity for faster retrieval.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the message body for given image ID"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @GetMapping(ImagePath.ROOT + "/{uri}")
    ResponseEntity<byte[]> displayImageData(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                            @PathVariable("uri") String uri);

    @Operation(
            summary = "Download image",
            description = "Redirects browser to download image with given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the message for given image ID"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @GetMapping(ImagePath.ROOT + "/{uri}/download")
    ResponseEntity<byte[]> getImageData(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                        @PathVariable("uri") String uri);

    @Operation(
            summary = "Return list of all images",
            description = "Returns all images info in the form of ImageDTO list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all available images"),
    })
    @GetMapping(ImagePath.ROOT + "s")
    ResponseEntity<List<ImageDTO>> getAllImages();

    @Operation(
            summary = "Upload image",
            description = "Saves new image entity in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved new entity in the database"),
            @ApiResponse(responseCode = "400", description = "Couldn't access image data bytes"),
    })
    @PostMapping(path = ImagePath.ROOT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> uploadImage(@Parameter(name = "file details", description = "ImageUploadDetails object")
                                  @RequestPart ImageUploadDetails fileDetails,
                                  @Parameter(name = "file", description = "MultipartFile object, actual image file")
                                  @RequestPart MultipartFile file);

    @Operation(
            summary = "Delete image",
            description = "Deletes image with given ID from the database. " +
                    "Also deletes all ScheduledImage entities that were associated with this image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the entity with given ID from the database"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @DeleteMapping(ImagePath.ROOT + "/{uri}")
    ResponseEntity<?> deleteImage(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                  @PathVariable("uri") String uri);

    @Operation(
            summary = "Update image",
            description = "Replaces old entity with the entity created from provided details, retaining the same ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted entity with given ID in the database"),
            @ApiResponse(responseCode = "400", description = "Couldn't access image data bytes"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @PutMapping(value = ImagePath.ROOT + "/{uri}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> updateImage(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                  @PathVariable("uri") String uri,
                                  @Parameter(name = "file details", description = "ImageUploadDetails object")
                                  @RequestPart ImageUploadDetails fileDetails,
                                  @Parameter(name = "file", description = "MultipartFile object, actual image file")
                                  @RequestPart MultipartFile file);

    @Operation(
            summary = "Patch image details",
            description = "Modifies entity with the details provided in the request, " +
                    "null request fields are not processed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted entity with given ID in the database"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @PatchMapping(ImagePath.ROOT + "/{uri}/details")
    ResponseEntity<?> updateImageDetails(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                         @PathVariable("uri") String uri,
                                         @Parameter(name = "file details", description = "ImageUploadDetails object")
                                         @RequestBody ImageUploadDetails newDetails);

    @Operation(
            summary = "Patch image data",
            description = "Modifies entity with the image data provided in the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted entity with given ID in the database"),
            @ApiResponse(responseCode = "400", description = "Couldn't access image data bytes"),
            @ApiResponse(responseCode = "404", description = "Image with given ID not found in the database"),
    })
    @PatchMapping(value = ImagePath.ROOT + "/{uri}/data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> updateImageData(@Parameter(name = "uri", description = "Unique Image entity identifier")
                                      @PathVariable("uri") String uri,
                                      @Parameter(name = "file", description = "MultipartFile object, actual image file")
                                      @RequestPart MultipartFile file);
}
