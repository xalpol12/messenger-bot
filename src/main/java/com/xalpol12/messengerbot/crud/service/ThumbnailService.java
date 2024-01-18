package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.exception.ImageAccessException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ThumbnailService {

    public byte[] generateThumbnail(byte[] fullImage, int thumbnailWidth, int thumbnailHeight) {
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(fullImage);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            Thumbnails.of(bis)
                    .size(thumbnailWidth, thumbnailHeight)
                    .outputFormat("jpg")
                    .toOutputStream(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ImageAccessException(e.getMessage());
        }
    }
}
