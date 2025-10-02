package com.sprint.mission.discodeit.entity;

/*
    * 디스코드 유저를 나타내는 엔티티
 */
public class User extends DefEntity{
    private String username;
    private String email;
    boolean active; // 활성화 여부

    // 생성자
    public User(String username, String email) {
        super();
        this.username = username;
        this.email = email;
        this.active = active;


    }


    // getter
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public void updateUsername(String newName) {
        this.username = newName;   // 새로운 이름으로 변경
        touch();                   // DefEntity의 updateAt을 현재 시간으로 갱신
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;     // 새로운 이메일로 변경
        touch();                   // 수정했으니까 updateAt 갱신
    }

    public void deactivate() {
        this.active = false;       // 계정을 비활성화
        touch();                   // 수정 시간 갱신
    }





}
