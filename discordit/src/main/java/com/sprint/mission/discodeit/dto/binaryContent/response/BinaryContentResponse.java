package com.sprint.mission.discodeit.dto.binaryContent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse (
        UUID id,
        Instant uploadedAt,
        String fileUrl
) {
    public static BinaryContentResponse toDto(BinaryContent content) {
        return new BinaryContentResponse(
                content.getId(),
                content.getUploadedAt(),
                content.getFileUrl()
        );
    }
}
