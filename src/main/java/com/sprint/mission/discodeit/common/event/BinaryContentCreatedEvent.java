package com.sprint.mission.discodeit.common.event;

import java.util.UUID;

public record BinaryContentCreatedEvent(
        UUID binaryContentId,
        byte[] data
) {
    public BinaryContentCreatedEvent {
        if (binaryContentId == null) {
            throw new IllegalArgumentException("binaryContentId is null");
        }

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data is null or empty");
        }
    }
}
