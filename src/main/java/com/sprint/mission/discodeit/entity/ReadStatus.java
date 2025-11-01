package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@Builder
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
        this.readAt = Instant.now();
    }
}
