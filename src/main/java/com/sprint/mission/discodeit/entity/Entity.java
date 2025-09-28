package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Entity {

    private UUID id;
    private long createdAt;
    private long updatedAt;

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = 0L;
    }

    public void updateEntity(){
        this.updatedAt = System.currentTimeMillis();
    }
}
