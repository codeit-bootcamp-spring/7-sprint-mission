package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        Instant createAt,
        String fileName,
        long size,
        String contentType

) {
    public static BinaryContentResponse from(BinaryContent content){
        return new BinaryContentResponse(content.getId(),
                content.getCreatedAt(),
                content.getFileName(),
                content.getFileSize(),
                content.getFileType());
    }
}
