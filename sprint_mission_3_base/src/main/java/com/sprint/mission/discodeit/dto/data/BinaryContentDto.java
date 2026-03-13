package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        String status
) {
    public static BinaryContentDto from(BinaryContent b) {
        return new BinaryContentDto(
                b.getId(),
                b.getFileName(),
                b.getSize(),
                b.getContentType(),
                b.getStatus().name()
        );
    }
}