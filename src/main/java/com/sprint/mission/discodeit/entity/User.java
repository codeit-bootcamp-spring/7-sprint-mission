package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BasicEntity {

    private String username; // 유저 이름
    private String nickName; // 유저 닉네임
    private String email; // 이메일
    private String password; // 유저 비밀번호
    private UUID profileId;


    public User(String username, String nickName, String email, String password, UUID profileId) {
        super();
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }
    public void updateProfile(UUID NewProfileId) {
        this.profileId = NewProfileId;
        update();
    }

    public void updateInfo(String name, String nickName, String email){
        this.username = name;
        this.nickName = nickName;
        this.email = email;
        update();
    }
}