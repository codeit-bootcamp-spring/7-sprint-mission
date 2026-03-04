package com.sprint.mission.discodeit.event.dto;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentCreateFailedEvent (
        Instant failedTime,
        UUID contentId,
        Exception exception
) { }
