package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Getter
@ToString
public class BinaryContent implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final String contentType;
    private final byte[] data;
    private static final long serialVersionUID = 1L;

    public BinaryContent(String fileName, String contentType, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = Objects.requireNonNull(fileName).trim();
        if(this.fileName.isEmpty() || this.fileName.length() > 255) {
            throw new IllegalArgumentException("invalid file name");
        }
        this.contentType = Objects.requireNonNull(contentType);
        this.data = Objects.requireNonNull(data);
    }
}
