package com.xalpol12.messengerbot.crud.service;

import com.xalpol12.messengerbot.crud.exception.customexception.ImageAccessException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class for managing thumbnails.
 */
@Service
public class ThumbnailService {

    /**
     * Generates a thumbnail based on provided image and dimensions
     * @param fullImage byte[] actual image data
     * @param thumbnailWidth desired width of the generated thumbnail
     * @param thumbnailHeight desired height of the generated thumbnail
     * @return byte[] generated thumbnail data
     */
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
