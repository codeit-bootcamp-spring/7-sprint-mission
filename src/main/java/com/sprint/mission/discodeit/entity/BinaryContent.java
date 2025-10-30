package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private final byte[] content; // 파일 데이터

    public BinaryContent(byte[] content) {
        Instant now = Instant.now();
        this.id = UUID.randomUUID(); // 랜덤 UUID로 초기화
        this.createdAt = now;
        this.content = content;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", (" + content.length + " bytes)" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BinaryContent that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }
}
