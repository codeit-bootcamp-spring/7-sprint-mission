package com.sprint.mission.discodeit.event.listener.kafka;

import java.util.UUID;

public record S3UploadFailedEvent(
        String requestId,
        UUID binaryContentId,
        String errorMessage
) {
}
