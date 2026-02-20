package com.sprint.mission.discodeit.dto.binaryContent.response;

import com.sprint.mission.discodeit.entity.enums.BinaryContentStatus;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        Instant createdAt,
        String fileName,
        Long size,
        String contentType,
        BinaryContentStatus status
) {

}
