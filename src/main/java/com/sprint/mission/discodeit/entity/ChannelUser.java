package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class ChannelUser {

    private final UUID id;
    private String nickname;
    private Role role;

    public ChannelUser(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.role = Role.MEMBER;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
