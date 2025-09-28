package com.sprint.mission.discodeit.entity;

public class User {

    private String name;
    private String nickname;
    private String email;
    private boolean isOnline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public User(String name, String nickname, String email, boolean isOnline) {
        super();
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isOnline = isOnline;
    }
}
