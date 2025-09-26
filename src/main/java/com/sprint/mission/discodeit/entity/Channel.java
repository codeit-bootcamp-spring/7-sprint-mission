package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String name;
    private UUID ownerId; // 채널 소유자 User의 ID

    public Channel(String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.ownerId = ownerId;
    }

    // Getter 함수들
    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getName() { return name; }
    public UUID getOwnerId() { return ownerId; }

    // 필드 수정 함수
    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{id=" + id.toString().substring(0, 8) +
                ", name='" + name + "', ownerId=" + ownerId.toString().substring(0, 8) +
                ", updatedAt=" + updatedAt + '}';
    }
}