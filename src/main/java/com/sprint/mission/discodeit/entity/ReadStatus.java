package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class ReadStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    UUID userId;
    UUID channelId;
    Instant readAt;

    //Constructor
    public ReadStatus(UUID userId, UUID channelId, Instant readAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.readAt = Instant.now();
    }

    //userId와 channelId가 기존과 같으면 생성하지 않고 아래 함수만 실행
    public void updateReadAt(){
        this.readAt = Instant.now();
    }
}
