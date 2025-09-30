package com.sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public BaseEntity() {
        long now = System.currentTimeMillis();
        this.id = UUID.randomUUID(); // 랜덤 UUID로 초기화
        this.createdAt = now;
        this.updatedAt = now;
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

    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return  ", UUID=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt;
    }

    //id를 통해서만 동일한 객체인지 비교
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
