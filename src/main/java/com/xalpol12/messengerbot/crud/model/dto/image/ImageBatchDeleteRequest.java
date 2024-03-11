package com.xalpol12.messengerbot.crud.model.dto.image;

import lombok.Data;

import java.util.List;

@Data
public class ImageBatchDeleteRequest {
    private List<String> imageIds;
}
