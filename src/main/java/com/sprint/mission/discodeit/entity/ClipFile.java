package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class ClipFile extends BinaryContent{
    //Field
    private final UUID messageId;

    //Constructor
    ClipFile(byte[] data, UUID messageId) {
        super(data);
        this.messageId = messageId;
    }
}
