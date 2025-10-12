package com2.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * 모든 엔티티(User, Channel, Message)가 공통으로 가지는 필드와 동작을 정의한 추상 클래스
 */
public abstract class BaseEntity {
    protected UUID id;        // 객체 식별자 (고유값)
    protected Long createdAt; // 생성 시각 (Unix Time)
    protected Long updatedAt; // 마지막 수정 시각 (Unix Time)

    public BaseEntity() {
        this.id = UUID.randomUUID();           // 객체 생성 시 고유한 UUID 자동 생성
        this.createdAt = System.currentTimeMillis(); // 생성 시각 기록
        this.updatedAt = this.createdAt;       // 최초 수정 시각 = 생성 시각
    }

    // Getter
    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    // 수정 시점 갱신용 메소드
    protected void touch() {
        this.updatedAt = System.currentTimeMillis();
    }
}