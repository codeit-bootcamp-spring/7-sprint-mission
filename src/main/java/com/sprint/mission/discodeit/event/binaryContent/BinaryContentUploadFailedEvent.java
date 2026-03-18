package com.sprint.mission.discodeit.event.binaryContent;

import java.util.UUID;

public record BinaryContentUploadFailedEvent(
        String requestId,
        UUID binaryContentId,
        String error
) {
}
