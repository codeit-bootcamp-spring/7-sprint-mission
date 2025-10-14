package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends BasicEntity {

    private String username; // 유저 이름
    private String nickName; // 유저 닉네임


    public User(String username, String nickName) {
        super();
        this.username = username;
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }


    public String getNickName() {
        return nickName;
    }

    public void setUsername(String username) {
        this.username = username;
        update();
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
        update();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", id='" + getId() + '\'' +
                '}';
    }
}