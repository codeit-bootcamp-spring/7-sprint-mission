package com.sprint.mission.discodeit.global.event;

import java.util.UUID;

public record S3UploadFailEvent(
        UUID binaryContentId,
        String errorMessage
) {
}
