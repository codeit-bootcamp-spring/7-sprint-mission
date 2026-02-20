package com.sprint.mission.discodeit.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContentCreatedEvent {
    private final UUID binaryContentId;
    private final byte[] bytes;
}
