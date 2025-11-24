package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        String id,
        String fileName,
        long size,
        String contentType


) {
    public static BinaryContentResponse from(BinaryContent content){
        return new BinaryContentResponse(content.getId().toString(),
                content.getFileName(),
                content.getFileSize(),
                content.getFileType());
    }
}
