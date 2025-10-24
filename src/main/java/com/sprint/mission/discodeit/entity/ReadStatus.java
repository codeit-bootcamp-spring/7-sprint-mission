package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BasicEntity{

    private final UUID userId; //유저 ID
    private final UUID channelId; //채널 ID
    private Instant lastReadAt; // 마지막 읽은 시간

    public ReadStatus(UUID userId, UUID channelId){
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    // 업데이트하기
    public void updateReadTime(){
        this.lastReadAt = Instant.now();
    }
}
