package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    // 도메인 필드
    private UUID userId;
    private Instant lastSeenAt;

    // 생성자: userId만 주는 경우
    public UserStatus(UUID userId) {
        this(userId, Instant.now());
    }

    // 생성자: userId, lastSeenAt 지정
    public UserStatus(UUID userId, Instant lastSeenAt) {
        this.id = UUID.randomUUID();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.userId = userId;
        this.lastSeenAt = (lastSeenAt != null) ? lastSeenAt : now;
    }

    /**
     * 서비스에서 기대하는 갱신 메서드
     * (BasicUserStatusService에서 us.update(Instant) 호출)
     */
    public void update(Instant lastSeenAt) {
        this.lastSeenAt = (lastSeenAt != null) ? lastSeenAt : Instant.now();
        this.updatedAt = Instant.now();
    }

    /** 편의 메서드: 특정 기간 내 접속 여부 */
    public boolean isOnlineWithin(Duration within, Instant now) {
        Instant pivot = now.minus(within);
        return !this.lastSeenAt.isBefore(pivot);
    }
}
