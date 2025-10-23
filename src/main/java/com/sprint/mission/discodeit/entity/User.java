package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * ✅ User 엔티티
 * - 디스코드 사용자 정보를 나타냄
 * - 공통 필드(id, createdAt, updatedAt)는 DefEntity(부모)로부터 상속받음
 */
public class User extends DefEntity {
    private static final long serialVersionUID = 1L;
    private String username;
    private String email;
    private boolean active; // 활성화 여부

    // --- 생성자 ---
    public User(String username, String email, boolean active) {
        super(); // DefEntity의 id, createdAt, updatedAt 초기화
        this.username = username;
        this.email = email;
        this.active = active;
    }

    // --- 2인자 생성자 오버로딩 ---
    public User(String username, String email) {
        this(username, email, true); // 기본값 active=true
    }

    // --- Getter ---
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }

    // --- Update 메서드 ---
    /**
     * 이름 변경
     */
    public void updateUsername(String newName) {
        this.username = newName;
        touch(); // updatedAt 갱신
    }

    /**
     * 이메일 변경
     */
    public void updateEmail(String newEmail) {
        this.email = newEmail;
        touch(); // updatedAt 갱신
    }

    /**
     * 활성화 상태 변경 (setter 역할)
     * - JCFUserService에서 호출
     */
    public void setActive(boolean active) {
        this.active = active;
        touch(); // 상태 변경 시 수정 시간도 업데이트
    }
}