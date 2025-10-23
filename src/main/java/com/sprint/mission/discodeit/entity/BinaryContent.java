package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;


@Getter
@ToString
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final String contentType;
    private final byte[] data;

    public BinaryContent(String fileName, String contentType, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = VerifiedUtils.verifyName(fileName);
        this.contentType = VerifiedUtils.verifyNull(contentType);
        this.data = VerifiedUtils.verifyNull(data);
    }
}
