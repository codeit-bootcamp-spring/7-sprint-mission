package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID userId;
    private final UUID channelId;
    private Instant readAt;

    //Constructor
    public ReadStatus(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

    //사용자가 채널을 읽음.
    public void updateReadAt(){
        super.update();
        this.readAt = Instant.now();
    }
}
