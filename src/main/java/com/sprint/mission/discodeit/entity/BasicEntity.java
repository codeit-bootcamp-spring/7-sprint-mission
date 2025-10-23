package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public abstract class BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public BasicEntity() {
        this.id = UUID.randomUUID(); // 랜덤 ID값
        this.createdAt = Instant.now(); //현재 시간 저장
        this.updatedAt = createdAt; // 수정한 시간을 지금 시간으로 업데이트
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update() {
        this.updatedAt = Instant.now(); // 수정된 시간 업데이트
    }
}