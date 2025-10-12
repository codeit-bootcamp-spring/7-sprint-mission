package com2.sprint.mission.discodeit.entity;

/**
 * 디스코드 유저를 나타내는 엔티티
 */
public class User extends BaseEntity {
    private String username; // 닉네임
    private String email;    // 이메일

    public User(String username, String email) {
        super(); // id, createdAt, updatedAt 초기화
        this.username = username;
        this.email = email;
    }

    // Getter
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // 정보 수정
    public void update(String username, String email) {
        this.username = username;
        this.email = email;
        touch(); // 수정 시점 갱신
    }
}