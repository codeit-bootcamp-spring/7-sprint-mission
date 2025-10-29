package com.sprint.mission.discodeit.dto.binary;

import java.util.UUID;

public record BinaryContentCreateRequest(
        String filename,
        String contentType,
        byte[] data,
        UUID userId,
        UUID messageId
) {}
