package com.sprint.mission.discodeit.dto.Binarycontent.response;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        long size,
        String contentType
) {
}
