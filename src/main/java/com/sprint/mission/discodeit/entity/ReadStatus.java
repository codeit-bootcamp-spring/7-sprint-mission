package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 사용자가 특정 채널에서 마지막으로 읽은 시간을 표현
 * - 공통 필드: id, createdAt, updatedAt (Instant)
 * - 참조: userId, channelId (의존 방향 = ID 참조)
 */
@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final UUID channelId;

    /** 공통 필드 */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;

    /** 마지막 읽음 시각 (메시지 읽음 기준 시간) */
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.lastReadAt = now;
    }

    /** 마지막 읽음 시각 갱신 */
    public void markRead(Instant when) {
        this.lastReadAt = when;
        this.updatedAt = when;
    }
}