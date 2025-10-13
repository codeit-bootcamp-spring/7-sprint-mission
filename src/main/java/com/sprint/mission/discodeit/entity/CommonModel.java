package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class CommonModel {
    private UUID id;
//    private Long createdAt;
//    private Long updatedAt; // 유닉스 타임스탬프
    private Instant createdAt;
    private Instant updatedAt; // 유닉스 타임스탬프

    public CommonModel() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now(); // System.currentTimeMillis();
        this.updatedAt = Instant.now(); // System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

//    public void setId(UUID id) {
//        this.id = id;
//    }

    public Instant getCreatedAt() {
        return createdAt;
    }

//    public void setCreatedAt(Long createdAt) {
//        this.createdAt = createdAt;
//    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = Instant.now(); // System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return  "\n id = " + id +
                ", \n createdAt = " + createdAt +
                ", \n updatedAt = " + updatedAt;
    }
}
