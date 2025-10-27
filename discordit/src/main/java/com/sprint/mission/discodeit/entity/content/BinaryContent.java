package com.sprint.mission.discodeit.entity.content;

import com.sprint.mission.discodeit.entity.base.User;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent{
    private UUID id;
    private User uploadUser;
    private Instant createdAt;

    public BinaryContent(User uploadUser) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.uploadUser = uploadUser;
    }
}
