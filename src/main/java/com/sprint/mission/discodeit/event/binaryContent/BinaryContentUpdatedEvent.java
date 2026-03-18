package com.sprint.mission.discodeit.event.binaryContent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentUpdatedEvent{
    private final UUID binaryId;
    private final UUID userId;
}
