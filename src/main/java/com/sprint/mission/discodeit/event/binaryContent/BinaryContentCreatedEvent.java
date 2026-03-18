package com.sprint.mission.discodeit.event.binaryContent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreatedEvent {
    private final UUID binaryId;
    private final byte[] bytes;
    private final UUID userId;
}
