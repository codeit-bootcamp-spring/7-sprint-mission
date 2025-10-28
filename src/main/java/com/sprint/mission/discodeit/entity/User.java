package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User extends BasicEntity {

    private String username; // 유저 이름
    private String nickName; // 유저 닉네임
    private String email; // 이메일
    private String password; // 유저 비밀번호
    private UUID profileId;


    public User(String username, String nickName, String email, String password) {
        super();
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
        update();
    }

    public void setUsername(String username) {
        this.username = username;
        update();
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        update();
    }

    public void setEmail(String email) {
        this.email = email;
        update();
    }


}