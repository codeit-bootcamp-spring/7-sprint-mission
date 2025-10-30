package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.User;
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
}
