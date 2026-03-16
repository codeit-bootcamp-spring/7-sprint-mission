package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;

public record BinaryContentUpdatedEvent(
        BinaryContentDto binaryContentDto
) {
}