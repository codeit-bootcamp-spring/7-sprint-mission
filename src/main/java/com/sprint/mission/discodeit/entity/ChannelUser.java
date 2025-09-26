package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class ChannelUser {
    private final UUID Id;
    private final String createdAt;
    private String updatedAt;
    private UUID channelId;
    private UUID userId;
    private Role role;

    public ChannelUser(UUID id, String createdAt) {
        Id = id;
        this.createdAt = createdAt;
    }
}
