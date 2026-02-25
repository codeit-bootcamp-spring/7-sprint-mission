package com.sprint.mission.discodeit.event.listener.kafka;

import java.util.UUID;

public record S3UploadFailedEvent(
        UUID binaryContentId
) {
}
