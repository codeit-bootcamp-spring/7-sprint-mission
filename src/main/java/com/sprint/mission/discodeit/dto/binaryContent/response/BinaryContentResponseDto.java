package com.sprint.mission.discodeit.dto.binaryContent.response;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType
) {

}
