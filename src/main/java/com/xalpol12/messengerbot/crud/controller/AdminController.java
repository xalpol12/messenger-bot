package com.xalpol12.messengerbot.crud.controller;

import com.xalpol12.messengerbot.crud.model.dto.ImageConfirmationDTO;
import com.xalpol12.messengerbot.crud.model.dto.ImageUploadRequest;
import com.xalpol12.messengerbot.crud.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final ImageService imageService;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageConfirmationDTO> addNewImage(@RequestBody ImageUploadRequest imageUploadRequest) {
        ImageConfirmationDTO imageConfirmationDTO = imageService.store(imageUploadRequest);
        return new ResponseEntity<>(imageConfirmationDTO, HttpStatus.CREATED);
    }
}
