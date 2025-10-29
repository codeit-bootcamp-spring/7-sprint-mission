package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class ProfileImg extends BinaryContent{
    //Field
    private final UUID ownerId;

    //Constructor
    ProfileImg(byte[] data, UUID ownerId) {
        super(data);
        this.ownerId = ownerId;
    }
}
