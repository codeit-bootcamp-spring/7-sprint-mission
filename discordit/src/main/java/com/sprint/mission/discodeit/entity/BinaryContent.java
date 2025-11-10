package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent{
    private UUID id;
    private Instant uploadedAt;
    private String fileUrl;

    public BinaryContent(String fileUrl) {
        this.id = UUID.randomUUID();
        this.uploadedAt = Instant.now();
        this.fileUrl = fileUrl;
    }

    public static BinaryContent fromDto(UUID id, Instant uploadedAt, String fileUrl) {
        BinaryContent c = new BinaryContent(fileUrl);
        c.id = id;
        c.uploadedAt = uploadedAt;
        return c;
    }
}
