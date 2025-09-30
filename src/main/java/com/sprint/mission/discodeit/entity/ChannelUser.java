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


}
