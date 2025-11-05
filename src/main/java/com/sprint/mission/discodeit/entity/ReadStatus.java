package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;
/*
    사용자가 채널 별 마지막으로 메세지를 읽은 시간을 표현하는 도메인 모델.
    사용자별 각 채널에 읽지 않은 메세지를 확인하기 위해 활용
 */
@Getter
@ToString
public class ReadStatus extends BaseEntity {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = VerifiedUtils.verifyNull(userId);
        this.channelId = VerifiedUtils.verifyNull(channelId);
        this.lastReadAt = lastReadAt;
    }

    public void readNow() {
        this.lastReadAt = Instant.now();
        reUpdatedAt();
    }

    public void readAt(Instant at) {
        Instant time = VerifiedUtils.verifyNull(at);
        if(time.isAfter(this.lastReadAt)) {
            this.lastReadAt = time;
            reUpdatedAt();
        }
    }
}
