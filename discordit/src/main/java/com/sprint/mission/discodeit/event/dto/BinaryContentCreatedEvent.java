package com.sprint.mission.discodeit.event.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record BinaryContentCreatedEvent(
        BinaryContent content,
        byte[] file
) {
}
