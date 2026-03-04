package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.status.BinaryContentStatus;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        BinaryContentStatus status
) {
}
