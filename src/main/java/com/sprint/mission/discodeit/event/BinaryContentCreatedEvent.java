package com.sprint.mission.discodeit.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class BinaryContentCreatedEvent extends ApplicationEvent {
    private final UUID binaryContentId;
    private final byte[] data;

    public BinaryContentCreatedEvent(Object source, UUID binaryContentId, byte[] data) {
        super(source);
        this.binaryContentId = binaryContentId;
        this.data = data;
    }
}
