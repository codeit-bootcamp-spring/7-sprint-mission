package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {
    private Instant uploadedAt;
    private String fileUrl;

    public BinaryContent(String fileUrl) {
        this.uploadedAt = Instant.now();
        this.fileUrl = fileUrl;
    }

    public static BinaryContent fromDto(UUID id, Instant createdAt, Instant updatedAt,
                                        Instant uploadedAt, String fileUrl) {
        BinaryContent c = new BinaryContent(fileUrl);
        c.uuid = id;
        c.createdAt = createdAt;
        c.updatedAt = updatedAt;
        c.uploadedAt = uploadedAt;
        return c;
    }
}
