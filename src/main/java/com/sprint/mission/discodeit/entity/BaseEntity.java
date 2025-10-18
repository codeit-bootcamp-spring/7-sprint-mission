package com.sprint.mission.discodeit.entity;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
/*
1. id : 객체를 식별하기 위한 id로 UUID 타입으로 선언
2. createAt, updateAt: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로
나타내기 위한 필드로 long 타입으로 선언한다.
 */

public abstract class BaseEntity implements Serializable {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private static final long serialVersionUID = 1L;

    protected BaseEntity() {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        updatedAt = createdAt;
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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{ id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    protected void reUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }
}
