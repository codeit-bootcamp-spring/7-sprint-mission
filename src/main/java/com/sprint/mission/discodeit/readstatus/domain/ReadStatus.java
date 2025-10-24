package com.sprint.mission.discodeit.readstatus.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

//용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
@Getter
public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 8L;

    private final UUID id;
    private final UUID userId;
    private final UUID serverId;
    private final Instant createdAt;
    private Instant lastReadAt;

    public static ReadStatus create(UUID userId, UUID serverId){
        return new ReadStatus(serverId,userId);
    }
    // 읽은 시간 갱신하는 메서드
    public void markAsRead(Instant lastReadAt){
        this.lastReadAt=lastReadAt;
    }

    private ReadStatus(UUID serverId, UUID userId) {
        this.id=UUID.randomUUID();
        this.serverId = serverId;
        this.userId = userId;
        this.createdAt=Instant.now();
        this.lastReadAt=Instant.now();
    }

    public long minutesSinceLastRead(Instant now) {
        return Duration.between(lastReadAt, now).toMinutes();
    }



}
