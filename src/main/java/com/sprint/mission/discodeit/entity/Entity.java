package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.time.Instant.*;

@Getter
public class Entity implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = createdAt;
    }
//    public Entity(UUID id) {
//        this.id = id;
//        this.createdAt = now();
//        this.updatedAt = createdAt;
//    }
//    public Entity(UUID id, long updatedAt) {
//        this.id = id;
//        this.createdAt = now();
//        this.updatedAt =createdAt;
//    }


    public void updateEntity(){
        this.updatedAt =now();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entity entity)) return false;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
