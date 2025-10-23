package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
/*
    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
    사용자의 온라인 상태를 확인하기 위해 활용
    마지막 접속 시간이 현재 시간으로부터 5분 이내면 현재 접속 중인 유저!
 */
@Getter
@ToString
public class UserStatus extends BaseEntity {
    private final UUID userId;
    private Instant lastReadAt;

    public UserStatus(UUID userId) {
        this.userId = VerifiedUtils.verifyNull(userId);
        this.lastReadAt = Instant.now();
    }

    public void timeUpdated() {
        this.lastReadAt = Instant.now();
        reUpdatedAt();
    }

    public boolean isOnlineNow() {
        Instant time = Instant.now().minus(Duration.ofMinutes(5));
        return lastReadAt.isAfter(time);
    }
}
