package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * 모든 엔티티(User, Channel, Message)가 공통으로 가지는 필드와 동작을 정의한 추상 클래스
 */
public abstract class DefEntity {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    protected DefEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {return id;}
    public Long getCreatedAt() {return createdAt;}
    public Long getUpdatedAt() {return updatedAt;}

    protected void touch() {
        this.updatedAt = System.currentTimeMillis();
    }



}
