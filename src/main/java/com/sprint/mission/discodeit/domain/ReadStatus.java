package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

//용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
@Getter
public class ReadStatus {

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.lastReadAt = lastReadAt;
        this.channelId = channelId;
    }

    public void read(){
        this.lastReadAt=Instant.now();
    }

    public Long timeSinceLastRead() {
        if (lastReadAt == null) {
            return null;
        }
        return Duration.between(lastReadAt, Instant.now()).toMinutes();
    }
}
