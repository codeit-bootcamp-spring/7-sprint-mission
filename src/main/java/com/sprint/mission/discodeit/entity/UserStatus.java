package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 사용자의 마지막 접속(확인) 시간
 * - 공통 필드: id, createdAt, updatedAt (Instant)
 * - 참조: userId
 * - 요구: 현재 접속 중 판별 메서드 (5분 이내면 온라인)
 */
@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;

    /** 공통 필드 */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;

    /** 마지막 접속 시간 */
    private Instant lastSeenAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.lastSeenAt = now;
    }

    public void seenNow() {
        Instant now = Instant.now();
        this.lastSeenAt = now;
        this.updatedAt = now;
    }

    /** 기본 규칙: 5분 이내면 온라인 */
    public boolean isOnlineNow() {
        return isOnlineWithin(Duration.ofMinutes(5));
    }

    /** 커스텀 임계치로도 판별 가능(테스트/확장용) */
    public boolean isOnlineWithin(Duration threshold) {
        if (lastSeenAt == null) return false;
        Instant cutoff = Instant.now().minus(threshold);
        return lastSeenAt.isAfter(cutoff) || lastSeenAt.equals(cutoff);
    }
}