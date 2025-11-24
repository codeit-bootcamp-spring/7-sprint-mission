package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class BinaryContent extends BaseEntity {
    private final String name;
    private String contentType;
    private final byte[] bytes; // 파일 데이터

    public BinaryContent(String name, String contentType, byte[] bytes) {
        this.name = name;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                ", name='" + name + '\'' +
                ", (" + bytes.length + " bytes)" +
                '}';
    }
}
