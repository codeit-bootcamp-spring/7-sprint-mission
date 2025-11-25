package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentUploadRequest(
        String fileName,
        byte[] bytes,
        String contentType,
        UUID messageId
) {}
