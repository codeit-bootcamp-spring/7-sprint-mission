package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {

    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    public static final long DEFAULT_UPDATED_AT = 0L;
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
        this.updatedAt = DEFAULT_UPDATED_AT;
    }
    public Entity(UUID id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = DEFAULT_UPDATED_AT;
    }


    public void updateEntity(){
        this.updatedAt = System.currentTimeMillis();
    }
}
