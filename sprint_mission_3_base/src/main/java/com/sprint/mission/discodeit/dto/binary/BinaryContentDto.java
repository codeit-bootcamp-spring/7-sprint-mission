package com.sprint.mission.discodeit.dto.binary;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String filename,
        String contentType,
        byte[] data,
        UUID userId,
        com.sprint.mission.discodeit.entity.BinaryOwnerType messageId
) {}

