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
    private final String name;
    private final Instant createdAt;
    private String contentType;
    private final byte[] bytes; // 파일 데이터

    public BinaryContent(String name, String contentType, byte[] bytes) {
        Instant now = Instant.now();
        this.id = UUID.randomUUID(); // 랜덤 UUID로 초기화
        this.createdAt = now;
        this.name = name;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", (" + bytes.length + " bytes)" +
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
