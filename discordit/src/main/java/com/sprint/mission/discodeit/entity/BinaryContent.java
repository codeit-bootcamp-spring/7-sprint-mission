package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent{
    private UUID id;
    private User uploadUser;
    private Instant uploadedAt;
    private String fileUrl;

    public BinaryContent(User uploadUser, String fileUrl) {
        this.id = UUID.randomUUID();
        this.uploadedAt = Instant.now();
        this.uploadUser = uploadUser;
        this.fileUrl = fileUrl;
    }

    public static BinaryContent fromDto(UUID id, Instant uploadedAt, User uploadUser, String fileUrl) {
        BinaryContent c = new BinaryContent(uploadUser, fileUrl);
        c.id = id;
        c.uploadedAt = uploadedAt;
        return c;
    }
}
