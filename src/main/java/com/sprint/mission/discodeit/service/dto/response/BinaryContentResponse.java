package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        Instant createAt,
        String fileName,
        long size,
        String contentType,
        byte[] bytes

) {
    public static BinaryContentResponse from(BinaryContent content,byte[] file){
        return new BinaryContentResponse(content.getId(),
                content.getCreatedAt(),
                content.getFileName(),
                content.getFileSize(),
                content.getFileType(),
                file);
    }
}
