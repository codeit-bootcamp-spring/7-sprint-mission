package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private long createdAt;
    private long updatedAt;

    public BasicEntity() {
        this.id = UUID.randomUUID(); // 랜덤 ID값
        this.createdAt = System.currentTimeMillis(); //현재 시간 저장
        this.updatedAt = createdAt; // 수정한 시간을 지금 시간으로 업데이트
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void update() {
        this.updatedAt = System.currentTimeMillis(); // 수정된 시간 업데이트
    }
}