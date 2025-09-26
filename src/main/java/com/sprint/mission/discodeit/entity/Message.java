package com.sprint.mission.discodeit.entity;

import java.util.UUID;

//DM
public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;


    private String[] record;
//    private 상태 비환성상태?


    public Message() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }



    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
