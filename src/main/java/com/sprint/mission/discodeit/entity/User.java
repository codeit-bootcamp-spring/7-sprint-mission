package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import java.io.Serializable;

public class User implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String email;

    // 생성자: id, createdAt, updatedAt 초기화 및 나머지 필드 초기화
    public User(String username, String email) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.username = username;
        this.email = email;
    }

    // Getter 함수들
    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // 필드 수정 함수
    public void update(String username, String email) {
        this.username = username;
        this.email = email;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }

    @Override
    public String toString() {
        return "User{id=" + id.toString().substring(0, 8) +
                ", username='" + username + "', email='" + email +
                ", updatedAt=" + updatedAt + '}';
    }
}