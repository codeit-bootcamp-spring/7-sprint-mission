package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private UUID userId;
    private UUID channelId;
    private Instant readAt;

    //Constructor
    private ReadStatus(UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

    //Factory Method
    public static ReadStatus create(UUID userId, UUID channelId){
        return new ReadStatus(userId, channelId);
    }

    //사용자가 채널을 읽음.
    public void updateReadAt(){
        super.update();
        this.readAt = Instant.now();
    }
}
