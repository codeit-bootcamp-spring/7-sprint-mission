package com.sprint.mission.discodeit.entity;

import java.util.UUID;

//친구 정보를
public class FriendUser {
    private UUID id;
    private String Username;
    private String nickname;


    public FriendUser(User user) {
        this.id = user.getId();
        Username = user.getUsername();
        this.nickname = user.getNickname();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return Username;
    }

    public String getNickname() {
        return nickname;
    }
}
