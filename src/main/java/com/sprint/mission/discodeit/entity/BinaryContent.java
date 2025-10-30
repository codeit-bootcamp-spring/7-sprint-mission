package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.*;

@Builder
@Getter
public class BinaryContent implements Serializable {
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = now();
    private byte [] binaryFile;
    private BinaryContentUsage binaryContentUsage;
}
