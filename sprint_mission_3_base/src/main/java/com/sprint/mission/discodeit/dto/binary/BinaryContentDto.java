package com.sprint.mission.discodeit.dto.binary;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        long size,
        String contentType,
        UUID messageId
) {
    public static BinaryContentDto from(BinaryContent b) {
        return new BinaryContentDto(
                b.getId(),
                b.getFileName(),
                b.getSize(),
                b.getContentType(),
                b.getMessage() != null ? b.getMessage().getId() : null
        );
    }
}
