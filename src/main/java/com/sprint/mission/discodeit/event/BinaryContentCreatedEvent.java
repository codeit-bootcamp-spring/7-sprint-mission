package com.sprint.mission.discodeit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreatedEvent {

    private final String filename;
    private final UUID binaryContentId;
    private final String contentType;
    private final byte [] bytes;

}
